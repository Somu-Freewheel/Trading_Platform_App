package com.example.trading_app.request;

import com.example.trading_app.domain.VerificationType;

import lombok.Data;

@Data
public class ForgotPasswordTokenRequest {
	private String sendTo;
	private VerificationType verificationType;

}
