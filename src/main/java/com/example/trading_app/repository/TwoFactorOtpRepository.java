package com.example.trading_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.trading_app.Entity.TwoFactorOTP;

import com.example.trading_app.Entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface TwoFactorOtpRepository extends JpaRepository<TwoFactorOTP,String> {
	
	TwoFactorOTP findByUserId(Long userId);
}




