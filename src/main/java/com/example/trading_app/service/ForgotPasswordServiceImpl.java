 package com.example.trading_app.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.trading_app.Entity.ForgotPasswordToken;
import com.example.trading_app.Entity.User;
import com.example.trading_app.domain.VerificationType;
import com.example.trading_app.repository.ForgotPasswordRepository;

@Service
public class ForgotPasswordServiceImpl implements ForgotPasswordService{
	private ForgotPasswordRepository forgotPasswordRepository;

	@Override
	public ForgotPasswordToken createToken(User user, String id,String otp, VerificationType verificationType, String sendTo) {
		// TODO Auto-generated method stub
		ForgotPasswordToken token = new ForgotPasswordToken();
		token.setUser(user);
		token.setSendTo(sendTo);
		token.setId(id);
		token.setOtp(otp);
		token.setVerificationType(verificationType);
		return forgotPasswordRepository.save(token);
	}

	@Override
	public ForgotPasswordToken findById(String id) {
		// TODO Auto-generated method stub
		Optional<ForgotPasswordToken> token = forgotPasswordRepository.findById(id);
		return token.orElse(null);
	}

	@Override
	public ForgotPasswordToken findByUser(Long userId) {
		// TODO Auto-generated method stub
		
		return forgotPasswordRepository.findByUserId(userId);
	}

	@Override
	public void deleteToken(ForgotPasswordToken token) {
		// TODO Auto-generated method stub
		forgotPasswordRepository.delete(token);
		
	}
	
	
	

}
