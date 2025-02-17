package com.example.trading_app.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import lombok.Data;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
@Entity
@Data
public class TwoFactorOTP {
	
	@Id
	private String Id;
	private String otp;
	@JsonProperty(access=JsonProperty.Access.WRITE_ONLY)
	@OneToOne
	private User user;
	@JsonProperty(access=JsonProperty.Access.WRITE_ONLY)
	private String jwt;
	

}
