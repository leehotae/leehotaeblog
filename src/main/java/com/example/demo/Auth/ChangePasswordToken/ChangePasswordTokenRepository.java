package com.example.demo.Auth.ChangePasswordToken;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;



public interface ChangePasswordTokenRepository  extends JpaRepository<ChangePasswordToken, Long>{

	
	Optional<ChangePasswordToken> findByToken(String token);
	Optional<ChangePasswordToken> findByEmail(String email);
	
}
