package com.authservice.controller;

import com.authservice.dto.APIResponse;
import com.authservice.dto.LoginDto;
import com.authservice.dto.UserDto;
import com.authservice.entity.User;
import com.authservice.service.AuthService;
import com.authservice.service.JWTService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	@Autowired
	private AuthService authService;
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private JWTService jwtService;

	@PostMapping("/register")
	public ResponseEntity<APIResponse<String>> register(@RequestBody UserDto userDto) {

		APIResponse<String> response = authService.register(userDto);

		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));

	}

	@PostMapping("/login")
	public ResponseEntity<APIResponse<String>> login(@RequestBody LoginDto loginDto) {
		APIResponse<String> response = new APIResponse<>();

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginDto.getUsername(),
				loginDto.getPassword());
		try {
			Authentication authenticate = authManager.authenticate(token);

			if (authenticate.isAuthenticated()) {
				String jwtToken = jwtService.generateToken(authenticate.getName(),
						authenticate.getAuthorities().iterator().next().getAuthority());

				response.setMessage("user login done");
				response.setStatus(201);
				response.setData(jwtToken);

				return new ResponseEntity<>(response, HttpStatus.OK);

			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		response.setMessage("Failed");
		response.setStatus(401);
		response.setData("Un-Authorized Access");
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);

	}
	// Feign-Client call
	@GetMapping("/get-user")
	public User getUser(@RequestParam String username) {
		User user = authService.findsByUsername(username);
		return user;
	}

}
