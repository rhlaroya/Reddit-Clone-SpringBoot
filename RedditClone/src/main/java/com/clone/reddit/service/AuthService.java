package com.clone.reddit.service;

import java.time.Instant;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.clone.reddit.dto.RegisterRequest;
import com.clone.reddit.model.User;
import com.clone.reddit.model.VerificationToken;
import com.clone.reddit.repository.UserRepository;
import com.clone.reddit.repository.VerificationTokenRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {
	
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final VerificationTokenRepository verificationRepository;

	@Transactional
	public void signup(RegisterRequest registerRequest) {
		User user  =  new User();
		user.setUsername(registerRequest.getUsername());
		user.setEmail(registerRequest.getEmail());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		user.setCreated(Instant.now());
		user.setEnabled(false);
		
		userRepository.save(user);
		
		String token = generateVerificationToken(user);
	}

	private String generateVerificationToken(User user) {
		String token = UUID.randomUUID().toString();
		VerificationToken verificationToken  = new VerificationToken();
		verificationToken.setToken(token);
		verificationToken.setUser(user);
		
		verificationRepository.save(verificationToken);
		return token;
		
	}
}
