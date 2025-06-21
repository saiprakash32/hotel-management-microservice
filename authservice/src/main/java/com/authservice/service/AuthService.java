package com.authservice.service;

import com.authservice.dto.APIResponse;
import com.authservice.dto.UserDto;
import com.authservice.entity.User;

import com.authservice.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public APIResponse<String> register(UserDto userDto) {
		APIResponse<String> response = new APIResponse<String>();
		if (userRepository.existsByUsername(userDto.getUsername())) {
		

			response.setMessage("Username already registered");
			response.setStatus(409);
			response.setData("registrationFailed");
			return response;
		}

		if (userRepository.existsByEmail(userDto.getEmail())) {
			

			response.setMessage("Email already registered");
			response.setStatus(409);
			response.setData("registration Failed");
			return response;

		}
		String encrypedPassword = passwordEncoder.encode(userDto.getPassword());
		
		User user = new User();
		BeanUtils.copyProperties(userDto, user);
		user.setPassword(encrypedPassword);
		user.setRole("ROLE_ADMIN");
		userRepository.save(user);

		response.setMessage("Registration Done");
		response.setStatus(201);
		response.setData("User is registered");

		return response;
	}

	public User findsByUsername(String username) {
		User user = userRepository.findByUsername(username);
		return user;
		
	}
}
