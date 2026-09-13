package com.example.trading_app.controller;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.example.trading_app.Utils.RedisConstants.OTP_TTL_MINUTES;
import static com.example.trading_app.Utils.RedisConstants.PENDING_USER_PREFIX;
import com.example.trading_app.config.RedisConfig;
import com.example.trading_app.dto.OtpVerificationRequestDto;
import com.example.trading_app.dto.PendingUserDto;
import com.example.trading_app.service.EmailServiceImpl;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.trading_app.Entity.TwoFactorOTP;
import com.example.trading_app.Entity.User;
import com.example.trading_app.Utils.OtpUtils;
import com.example.trading_app.config.JwtProvider;
import com.example.trading_app.repository.UserRepository;
import com.example.trading_app.service.UserService;
import com.example.trading_app.response.AuthResponse;
import com.example.trading_app.service.CustomUserDetailsService;
import com.example.trading_app.service.TwoFactorOtpService;


@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtProvider jwtProvider;
	@Autowired
	private UserService userService;
	@Autowired
	private TwoFactorOtpService twoFactorOtpService;
	@Autowired
	private EmailServiceImpl emailServiceImp;
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private EmailServiceImpl emailService;
	@PostMapping("/signup")
	public ResponseEntity<AuthResponse>registerUser(@RequestBody User user)throws Exception{
		Optional<User> isEmailExist=userRepository.findByEmail(user.getEmail());
		if(isEmailExist.isPresent()) {

			 throw new Exception("email is already used with another account");
		}
		String otp= OtpUtils.generateOtp();
		PendingUserDto pendingUser = new PendingUserDto(
				user.getFullName(),
				user.getEmail(),
				user.getPassword(),
				otp
		);
		String key = PENDING_USER_PREFIX + user.getEmail();
		redisTemplate.opsForValue().set(key, pendingUser, OTP_TTL_MINUTES, TimeUnit.MINUTES);
		try {
			emailService.sendVerificationOtpEmail(user.getEmail(), otp);
		} catch (MessagingException e) {
			redisTemplate.delete(key); // don't leave a dangling pending user if email failed
			throw new Exception("Failed to send verification email. Please try signing up again.");
		}
		AuthResponse authResponse=new AuthResponse();
		authResponse.setStatus(true);
		authResponse.setMessage("OTP sent to your email. Please verify to complete registration.");
		return new ResponseEntity<>(authResponse, HttpStatus.OK);
	}
	@PostMapping("/verify-otp")
	public ResponseEntity<AuthResponse>verifyOtp(@RequestBody OtpVerificationRequestDto request) throws Exception {
		String key = PENDING_USER_PREFIX + request.getEmail();
		PendingUserDto pendingUser = (PendingUserDto) redisTemplate.opsForValue().get(key);
		if (pendingUser == null) {
			throw new Exception("OTP expired or invalid. Please sign up again.");
		}

		if (!pendingUser.getOtp().equals(request.getOtp())) {
			throw new Exception("Incorrect OTP.");
		}

		Optional<User> isEmailExist = userRepository.findByEmail(request.getEmail());
		if (isEmailExist.isPresent()) {
			redisTemplate.delete(key);
			throw new Exception("email is already used with another account");
		}

		User newUser = new User();
		newUser.setEmail(pendingUser.getEmail());
		newUser.setFullName(pendingUser.getFullName());
		newUser.setPassword(pendingUser.getEncodedPassword()); // raw for now, see TODO above

		User savedUser = userRepository.save(newUser);
		redisTemplate.delete(key); // one-time use, clean up

		Authentication auth = new UsernamePasswordAuthenticationToken(
				savedUser.getEmail(),
				null,
				Collections.emptyList()
		);
		SecurityContextHolder.getContext().setAuthentication(auth);
		String jwt = JwtProvider.generateToken(auth);

		AuthResponse authResponse = new AuthResponse();
		authResponse.setJwt(jwt);
		authResponse.setStatus(true);
		authResponse.setMessage("Register Successful");
		return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
	}


	@PostMapping("/signin")
	public ResponseEntity<AuthResponse>loginUser(@RequestBody User user)throws Exception{
		Optional<User> isEmailExist=userRepository.findByEmail(user.getEmail());
		
		String userName=user.getEmail();
		String password=user.getPassword();
		
		Authentication auth = authenticate(userName,password);
		SecurityContextHolder.getContext().setAuthentication(auth);
		String jwt=JwtProvider.generateToken(auth);
		// Use UserService so the lookup goes through the @Cacheable method and populates Redis
		User authUser = userService.findUserByEmail(userName);
		if(user.getTwoFactorAuth().isEnabled()) {
			AuthResponse authResponse=new AuthResponse();
			authResponse.setMessage("Two Factor auth is Enabled");
			authResponse.setTwoFactorAuthEnable(true);
			String otp=OtpUtils.generateOtp();
			TwoFactorOTP oldTwoFactorOtp=twoFactorOtpService.findByUserId(authUser.getId());
			//Optional.ofNullable(oldTwoFactorOtp).ifPresent(twoFactorOtpService::deleteTwoFactorOtp);
			if(oldTwoFactorOtp!=null) {
				twoFactorOtpService.deleteTwoFactorOtp(oldTwoFactorOtp);
			}
			TwoFactorOTP newTwoFactorOTP =twoFactorOtpService.createTwoFactorOtp(authUser, otp, jwt);
			authResponse.setSession(newTwoFactorOTP.getId());
			return new ResponseEntity<>(authResponse,HttpStatus.ACCEPTED);

		}
		AuthResponse authResponse=new AuthResponse();
		authResponse.setJwt(jwt);
		authResponse.setStatus(true);
		authResponse.setMessage("Login Successful");
		return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
	}
	private Authentication authenticate(String userName,String password) throws Exception{
		UserDetails userDetails =customUserDetailsService.loadUserByUsername(userName);
		if(userDetails==null) {
			throw new Exception("UserName Not Found. Pls Try Again!!!");
		}
		if(!password.equals(userDetails.getPassword())) {
			throw new Exception("Invalid Password. Pls Try Again!!!");
		}
		return new UsernamePasswordAuthenticationToken(userDetails,password,userDetails.getAuthorities());
		
	}
	@PostMapping("/two-factor/otp/{otp}")
	public ResponseEntity<AuthResponse>verifySignInOtp(
			@PathVariable String otp,
			@RequestParam String id) throws Exception{
		TwoFactorOTP twoFactorOTP=twoFactorOtpService.findById(id);
		if(twoFactorOtpService.verifyTwoFactorOtp(twoFactorOTP, otp)) {
			AuthResponse authResponse=new AuthResponse();
			authResponse.setMessage("Two factor authentication verified");
			authResponse.setTwoFactorAuthEnable(true);
			authResponse.setJwt(twoFactorOTP.getJwt());
			return new ResponseEntity<>(authResponse,HttpStatus.OK);				
		}
		throw new Exception("Invalid OTP!!");
	}


}
