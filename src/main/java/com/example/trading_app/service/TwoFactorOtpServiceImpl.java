package com.example.trading_app.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.trading_app.Entity.TwoFactorOTP;
import com.example.trading_app.Entity.User;
import com.example.trading_app.repository.TwoFactorOtpRepository;

public class TwoFactorOtpServiceImpl implements TwoFactorOtpService{
	@Autowired
	private TwoFactorOtpRepository twoFactorOtpRepository;
	
	@Override
	public TwoFactorOTP createTwoFactorOtp(User user,String otp,String jwt) {
		UUID uuid =UUID.randomUUID();
		String id=uuid.toString();
		TwoFactorOTP twoFactorOtp=new TwoFactorOTP();
		twoFactorOtp.setOtp(otp);
		twoFactorOtp.setJwt(jwt);
		twoFactorOtp.setId(id);
		twoFactorOtp.setUser(user);
		return twoFactorOtpRepository.save(twoFactorOtp);	
	}

	@Override
	public TwoFactorOTP findByUserId(Long userId) {
		
		return null;
	}

	@Override
	public TwoFactorOTP findById(String id) {
		Optional<TwoFactorOTP>optUserId=twoFactorOtpRepository.findById(id);
		return optUserId.orElse(null);	
	}

	@Override
	public boolean verifyTwoFactorOtp(TwoFactorOTP twoFactorOtp, String otp) {
		
		return twoFactorOtp.getOtp().equals(otp);
	}

	@Override
	public void deleteTwoFactorOtp(TwoFactorOTP twoFactorOtp) {
		twoFactorOtpRepository.delete(twoFactorOtp);
		
	}

}
