package com.example.trading_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.trading_app.Entity.VerificationCode;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode,Long>{
	public VerificationCode findByUserId(Long userId);

}
