package com.example.trading_app.Entity;

import com.example.trading_app.domain.USER_ROLE;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "trading_user")
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String fullName;
	private String email;
	@JsonProperty(access=JsonProperty.Access.WRITE_ONLY)
	private String password;
	@Embedded
	private TwoFactorAuth twoFactorAuth=new TwoFactorAuth();
	private USER_ROLE role=USER_ROLE.ROLE_CUSTOMER;
	// Convenience accessor for code that expects a getIsEnabled() method
	// Returns whether two-factor auth is enabled for this user (false if TFA not set)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	public boolean getIsEnabled() {
		return this.twoFactorAuth != null && this.twoFactorAuth.isEnabled();
	}
}
