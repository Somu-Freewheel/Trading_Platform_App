package com.example.trading_app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.trading_app.Entity.User;
import com.example.trading_app.repository.UserRepository;
@Service
public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> opuser=userRepository.findByEmail(username);
		if(Objects.isNull(opuser)) {
			throw new UsernameNotFoundException(username);
		}
		List<GrantedAuthority>authorityList=new ArrayList<>();
		User user =opuser.get();
		return new org.springframework.security.core.userdetails.User(
				user.getEmail(),
				user.getPassword(),
				authorityList);
	}
	
	
	

}
