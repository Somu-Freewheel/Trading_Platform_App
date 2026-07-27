package com.example.trading_app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import com.example.trading_app.Entity.TwoFactorAuth;
import com.example.trading_app.Entity.User;
import com.example.trading_app.config.JwtProvider;
import com.example.trading_app.domain.VerificationType;
import com.example.trading_app.repository.UserRepository;
@Service
public class UserServiceImpl implements UserService{
	@Autowired
	private UserRepository userRepository;

	@Override
	@Cacheable(value = "userCache", key = "#jwt")
	public User findUserProfileByJwt(String jwt) {
		String email=JwtProvider.getEmailFromJwtToken(jwt);
		Optional<User> user =userRepository.findByEmail(email);
		if(user.isEmpty()) {
			throw new RuntimeException("User not found");
		}
		return user.get();
	}

	@Override
	@Cacheable(value = "userCache", key = "#email")
	public User findUserByEmail(String email) {
		Optional<User> user =userRepository.findByEmail(email);
		if(user.isEmpty()) {
			throw new RuntimeException("User not found");
		}
		return user.get();
	}
	@Override
	@Cacheable(value = "userCache", key = "#userId")
	public User findUserById(Long userId) {
		Optional<User> user =userRepository.findById(userId);
		if(user.isEmpty()) {
			throw new RuntimeException("User not found");
		}
		return user.get();
	}
	
	@Override
	@CacheEvict(value = "userCache", allEntries = true)
	public User updatePassword(User user, String newPassword) {
		// TODO Auto-generated method stub
		user.setPassword(newPassword);
		return userRepository.save(user);
	}

	@Override
	@CacheEvict(value = "userCache", allEntries = true)
	public User enableTwoFactorAuthentication(VerificationType verificationType,String sendTo,User user) {
		// TODO Auto-generated method stub
		TwoFactorAuth twoFactorAuth=new TwoFactorAuth();
		twoFactorAuth.setEnabled(true);
		twoFactorAuth.setSendTo(verificationType);
		user.setTwoFactorAuth(twoFactorAuth);
		return userRepository.save(user);
	}
	

}
