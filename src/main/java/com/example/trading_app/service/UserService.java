package com.example.trading_app.service;

import com.example.trading_app.Entity.User;
import com.example.trading_app.domain.VerificationType;

public interface UserService {
	public User findUserProfileByJwt(String jwt);
	public User findUserByEmail(String email);
	public User findUserById(Long userId);
	public User enableTwoFactorAuthentication(VerificationType verificationType,String sendTo,User user);
	User updatePassword(User user ,String newPassword);
	

}
