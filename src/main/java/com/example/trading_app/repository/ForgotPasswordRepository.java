package com.example.trading_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.trading_app.Entity.ForgotPasswordToken;
import org.springframework.stereotype.Repository;

@Repository
public interface ForgotPasswordRepository extends JpaRepository <ForgotPasswordToken,String>{
	ForgotPasswordToken findByUserId(Long userId);

}
