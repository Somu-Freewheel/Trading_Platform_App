package com.example.trading_app.service;

import com.example.trading_app.Entity.User;
import com.example.trading_app.Entity.VerificationCode;
import com.example.trading_app.domain.VerificationType;

public interface VerificationCodeService {
	VerificationCode sendVerificationCode(User user, VerificationType verificationType);
	VerificationCode getVerificationCodeById(Long id);
	VerificationCode getVerificationCodeByUser(Long userId);
	void deleteVerificationCodeById(VerificationCode verificationCode); 
		
	
	

}
