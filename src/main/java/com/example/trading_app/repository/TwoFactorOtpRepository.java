package com.example.trading_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.trading_app.Entity.TwoFactorOTP;
<<<<<<< HEAD
import com.example.trading_app.Entity.User;

public interface TwoFactorOtpRepository extends JpaRepository<TwoFactorOTP,String> {
	
	TwoFactorOTP findByUserId(Long userId);

=======

public interface TwoFactorOtpRepository extends JpaRepository<TwoFactorOTP,String>{
	TwoFactorOTP findByUserId(Long userId);
>>>>>>> 6b623dd (Implemented E-mail Service)
}
