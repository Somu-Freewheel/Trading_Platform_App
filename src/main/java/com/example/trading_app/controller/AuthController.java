package com.example.trading_app.controller;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.example.trading_app.response.AuthResponse;
import com.example.trading_app.service.CustomUserDetailsService;
import com.example.trading_app.service.EmailServiceImp;
import com.example.trading_app.service.TwoFactorOtpService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    private JwtProvider jwtProvider;
	@Autowired
	private TwoFactorOtpService twoFactorOtpService;
	@Autowired
	private EmailServiceImp emailServiceImp;
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	@PostMapping("/signup")
	public ResponseEntity<AuthResponse>registerUser(@RequestBody User user)throws Exception{
		Optional<User> isEmailExist=userRepository.findByEmail(user.getEmail());
		if(isEmailExist.isPresent()) {

			 throw new Exception("email is already used with another account");
		}
		User newUser = new User();
		newUser.setEmail(user.getEmail());
		newUser.setPassword(user.getPassword());
		newUser.setFullName(user.getFullName());
		User savedUser = userRepository.save(newUser);
		Authentication auth = new UsernamePasswordAuthenticationToken(
				user.getEmail(),
				user.getPassword()
				);
		SecurityContextHolder.getContext().setAuthentication(auth);
		String jwt=JwtProvider.generateToken(auth);
		AuthResponse authResponse=new AuthResponse();
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
		User authUser = userRepository.findByEmail(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));
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
