package com.example.trading_app.service;

import com.example.trading_app.Entity.TwoFactorOTP;
import com.example.trading_app.Entity.User;

public interface TwoFactorOtpService {
	TwoFactorOTP createTwoFactorOtp(User user,String otp,String jwt);
<<<<<<< HEAD
	TwoFactorOTP findByUserId(Long userId);
	TwoFactorOTP findById(String id);
	boolean verifyTwoFactorOtp(TwoFactorOTP teoFactorOtp, String otp);
	void deleteTwoFactorOtp(TwoFactorOTP teoFactorOtp);
=======
	TwoFactorOTP findByUser(Long userId);
	TwoFactorOTP findById(String id);
	boolean verifyTwoFactorOtp(TwoFactorOTP twoFactorOtp, String otp);
	void deleteTwoFactorOtp(TwoFactorOTP twoFactorOtp);
	
>>>>>>> 6b623dd (Implemented E-mail Service)

}
