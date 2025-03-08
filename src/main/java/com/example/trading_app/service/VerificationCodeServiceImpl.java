package com.example.trading_app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.trading_app.Entity.User;
import com.example.trading_app.Entity.VerificationCode;
import com.example.trading_app.Utils.OtpUtils;
import com.example.trading_app.domain.VerificationType;
import com.example.trading_app.repository.VerificationCodeRepository;

public class VerificationCodeServiceImpl implements VerificationCodeService{
	@Autowired
	private VerificationCodeRepository verificationCodeRepository;
	@Override
	public VerificationCode sendVerificationCode(User user, VerificationType verificationType) {
		// TODO Auto-generated method stub
		VerificationCode verificationCode1 = new VerificationCode();
		verificationCode1.setOtp(OtpUtils.generateOtp());
		verificationCode1.setVerificationType(verificationType);
		verificationCode1.setUser(user);
		return verificationCodeRepository.save(verificationCode1);
		
	}

	@Override
	public VerificationCode getVerificationCodeById(Long id) {
		// TODO Auto-generated method stub
		Optional<VerificationCode>verificationCode=verificationCodeRepository.findById(id);
		if(verificationCode.isPresent()) {
			return verificationCode.get();
		}
		throw new RuntimeException("Verification code not found");
	}

	@Override
	public VerificationCode getVerificationCodeByUser(Long userId) {
		// TODO Auto-generated method stub
		return verificationCodeRepository.findByUserId(userId);
	}

	@Override
	public void deleteVerificationCodeById(VerificationCode verificationCode) {
		// TODO Auto-generated method stub
		verificationCodeRepository.delete(verificationCode);
		
	}

	
	

}
