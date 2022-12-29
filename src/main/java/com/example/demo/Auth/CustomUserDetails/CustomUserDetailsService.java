package com.example.demo.Auth.CustomUserDetails;




import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import com.example.demo.User.User;
import com.example.demo.User.UserRepository;




public class CustomUserDetailsService implements UserDetailsService{
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		

		User user=userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("not existing user"));
		

		
		return new CustomUserDetails(user);
	}

  
	
}
