package com.example.trading_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.trading_app.Entity.ForgotPasswordToken;

public interface ForgotPasswordRepository extends JpaRepository <ForgotPasswordToken,String>{
	ForgotPasswordToken findByUserId(Long userId);

}
