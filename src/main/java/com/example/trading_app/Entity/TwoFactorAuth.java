package com.example.trading_app.Entity;

import com.example.trading_app.domain.VerificationType;

import lombok.Data;

@Data
public class TwoFactorAuth {
	private boolean isEnabled=false;
	private VerificationType sendTo;

}
