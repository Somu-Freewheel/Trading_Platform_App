package com.example.trading_app.service;

import com.example.trading_app.Entity.ForgotPasswordToken;
import com.example.trading_app.Entity.User;
import com.example.trading_app.domain.VerificationType;

public interface ForgotPasswordService {
	ForgotPasswordToken createToken(User user, String id,String otp, VerificationType verificationType, String sendTo);
	ForgotPasswordToken findById(String id);
	ForgotPasswordToken findByUser(Long userId);
	
	void deleteToken(ForgotPasswordToken token);

}
