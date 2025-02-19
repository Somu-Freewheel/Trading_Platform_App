package com.example.trading_app.Utils;

import java.util.Random;

public class OtpUtils {
<<<<<<< HEAD
	public static String generateOTP() {
		int otpLength=6;
		Random random=new Random();
=======
	public static String generateOtp() {
		int otpLength=6;
		Random random = new Random();
>>>>>>> 6b623dd (Implemented E-mail Service)
		StringBuilder otp=new StringBuilder(otpLength);
		for(int i=0;i<otpLength;i++) {
			otp.append(random.nextInt(10));
		}
		return otp.toString();
		
	}

}
