package com.example.trading_app.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
<<<<<<< HEAD
import lombok.Data;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
@Entity
@Data
public class TwoFactorOTP {
	
	@Id
	private String Id;
=======
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class TwoFactorOTP {
	@Id
	private String id;
>>>>>>> 6b623dd (Implemented E-mail Service)
	private String otp;
	@JsonProperty(access=JsonProperty.Access.WRITE_ONLY)
	@OneToOne
	private User user;
	@JsonProperty(access=JsonProperty.Access.WRITE_ONLY)
	private String jwt;
	
<<<<<<< HEAD

=======
	
	
	
>>>>>>> 6b623dd (Implemented E-mail Service)
}
