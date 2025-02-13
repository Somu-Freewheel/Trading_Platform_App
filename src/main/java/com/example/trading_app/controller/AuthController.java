package com.example.trading_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.trading_app.Entity.User;
import com.example.trading_app.repository.UserRepository;
@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	private UserRepository userRepository;
	@PostMapping("/signup")
	public ResponseEntity<User>registerUser(@RequestBody User user){
		User newUser = new User();
		newUser.setEmail(user.getEmail());
		newUser.setPassword(user.getPassword());
		newUser.setEmail(user.getEmail());
		newUser.setFullName(user.getFullName());
		
		User savedUser = userRepository.save(newUser);
		return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
	}

}
