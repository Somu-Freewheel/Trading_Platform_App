package com.example.trading_app.service;

import com.example.trading_app.Entity.TwoFactorOTP;
import com.example.trading_app.Entity.User;

public interface TwoFactorOtpService {
	TwoFactorOTP createTwoFactorOtp(User user,String otp,String jwt);
	TwoFactorOTP findByUserId(Long userId);
	TwoFactorOTP findById(String id);
	TwoFactorOTP findByUser(Long userId);
	boolean verifyTwoFactorOtp(TwoFactorOTP twoFactorOtp, String otp);
	void deleteTwoFactorOtp(TwoFactorOTP twoFactorOtp);
	

}
