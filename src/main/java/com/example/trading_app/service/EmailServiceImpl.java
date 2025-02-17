package com.example.trading_app.service;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl {
	private JavaMailSender javaMailSender;
	public void sendVerificationOtpEmail(String email,String otp) throws MessagingException{
		MimeMessage mimeMessage=javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage,"utf-8");
		String subject="Verify OTP";
		String text="Your Verification code is OTP";
		mimeMessageHelper.setSubject(subject);
		mimeMessageHelper.setText(text);
		mimeMessageHelper.setTo(email);
		try {
			javaMailSender.send(mimeMessage);
		}catch(MailException e) {
			throw new MailSendException(e.getMessage());
			
		}
	}

}
