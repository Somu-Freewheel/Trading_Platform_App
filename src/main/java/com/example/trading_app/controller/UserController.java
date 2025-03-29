package com.example.trading_app.controller;
import java.util.Optional;
import java.util.UUID;

import com.example.trading_app.service.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.trading_app.request.ForgotPasswordTokenRequest;
import com.example.trading_app.request.ResetPasswordRequest;
import com.example.trading_app.Entity.ForgotPasswordToken;
import com.example.trading_app.Entity.User;
import com.example.trading_app.Entity.VerificationCode;
import com.example.trading_app.Utils.OtpUtils;
import com.example.trading_app.domain.VerificationType;
import com.example.trading_app.response.ApiResponse;
import com.example.trading_app.response.AuthResponse;
import com.example.trading_app.service.ForgotPasswordService;
import com.example.trading_app.service.UserService;
import com.example.trading_app.service.VerificationCodeService;

@RestController
public class UserController {
	@Autowired
	private UserService userService;
	private VerificationCodeService verificationCodeService;
	@Autowired
	private EmailServiceImpl emailServiceImp;
	@Autowired 
	private ForgotPasswordService forgotPasswordService;
	@GetMapping("/api/users/profile")
	public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt){
		User user=userService.findUserProfileByJwt(jwt);
		return new ResponseEntity<User>(user,HttpStatus.OK);
	}
	//Enable Two-Factor Authentication
	@PostMapping("/api/users/verification/{verificationType}/send-otp")
	public ResponseEntity<String>sendVerificationOtp(
			@RequestHeader("Authorization") String jwt,
			@PathVariable VerificationType verificationType)throws Exception{
			User user =userService.findUserProfileByJwt(jwt);
			VerificationCode verificationCode=verificationCodeService.getVerificationCodeByUser(user.getId());
			if(verificationCode==null) {
				verificationCode=verificationCodeService.sendVerificationCode(user, verificationType);
			}
			if(verificationType.equals(VerificationType.EMAIL)) {
				emailServiceImp.sendVerificationOtpEmail(user.getEmail(), verificationCode.getOtp());
			}
			return new ResponseEntity<>("Verification Otp send successfully" ,HttpStatus.OK);	
	}
	@PatchMapping("/api/users/enable-two-factor/verify/verify-otp/{otp}")
	public ResponseEntity<User>enableTwoFactorAuthentication(
			@PathVariable String otp,
			@RequestHeader("Authorization") String jwt) throws Exception{
		User user =userService.findUserProfileByJwt(jwt);
		VerificationCode verificationCode=verificationCodeService.getVerificationCodeByUser(user.getId());
		String sendTo=verificationCode
				.getVerificationType()
				.equals(VerificationType.EMAIL)?
						verificationCode.getEmail():verificationCode.getMobile();
		boolean isVerified=verificationCode.getOtp().equals(otp);
		if(isVerified) {
			User updatedUser=userService.enableTwoFactorAuthentication(verificationCode.getVerificationType(), sendTo,user);
			verificationCodeService.deleteVerificationCodeById(verificationCode);
			return new ResponseEntity<>(updatedUser,HttpStatus.OK);
		}
		throw new Exception("Wrong OTP");
		
		
	}
	//Forgot Password Case 
	//Here we will send otp in case the password is forgot 
		@PostMapping("/auth/users/reset-password/send-otp")
		public ResponseEntity<AuthResponse>sendForgotPasswordOtp(
				@RequestBody ForgotPasswordTokenRequest tokenRequest)throws Exception{
				User user =userService.findUserByEmail(tokenRequest.getSendTo());
				String otp = OtpUtils.generateOtp();
				UUID uuid = UUID.randomUUID();
				String id = uuid.toString();
				ForgotPasswordToken token = forgotPasswordService.findByUser(user.getId());
				token = Optional.ofNullable(token)	
					.orElseGet(()->forgotPasswordService.createToken(user, id, otp,tokenRequest.getVerificationType(), tokenRequest.getSendTo()));	
				if(tokenRequest.getVerificationType().equals(VerificationType.EMAIL)) {
					emailServiceImp.sendVerificationOtpEmail(user.getEmail(),token.getOtp());
				}
				AuthResponse response = new AuthResponse();
				 response.setSession(token.getId());
				 response.setMessage("Password reset otp sent successfully");
				
				return new ResponseEntity<>(response ,HttpStatus.OK);	
		}
		
		@PatchMapping("/auth/users/reset-password/verify-otp")
		public ResponseEntity<ApiResponse>resetPassword(
				@RequestParam String id,
				@RequestBody ResetPasswordRequest request,
				@RequestHeader("Authorization") String jwt) throws Exception{
			
			ForgotPasswordToken forgotPasswordToken=forgotPasswordService.findById(id);
			
			boolean isVerified=forgotPasswordToken.getOtp().equals(request.getOtp());
			if(isVerified) {
				userService.updatePassword(forgotPasswordToken.getUser(), request.getPassword());
				ApiResponse response = new ApiResponse();
				response.setMessage("Password update Successfull");												
				return new ResponseEntity<>(response,HttpStatus.OK);
			}
			throw new Exception("Wrong OTP");						
		}	
}
