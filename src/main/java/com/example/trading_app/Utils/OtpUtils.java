 package com.example.trading_app.Utils;

import com.example.trading_app.Entity.User;

import java.util.Objects;
import java.util.Random;

public class OtpUtils {
	public static String generateOtp() {
		int otpLength=6;
		Random random = new Random();
		StringBuilder otp=new StringBuilder(otpLength);
		for(int i=0;i<otpLength;i++) {
			otp.append(random.nextInt(10));
		}
		return otp.toString();
		
	}
	//to validate the user
	public static void validateUser(User user) {
		Objects.requireNonNull(user, "User cannot be null");
		Long userId = user.getId();
		if (userId == null || userId <= 0) {
			throw new IllegalArgumentException("User ID must be a positive, non-null value");
		}
	}

}
