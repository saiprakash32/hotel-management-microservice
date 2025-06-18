package com.authservice.service;


import java.util.Date;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;


@Service
public class JWTService {
	
	private static final String SECRET_KEY="secret12345";
	private static final int EXPIRATION_TIME=86400000;
	
	
//	@Value("${jwt.key}")
//	private String secretKey;
//
//	@Value("${jwt.expiry-time}")
//	private int expiry;
	

	
	public String generateToken(String username, String authority) {
		
		return JWT.create()
				.withSubject(username)
				.withClaim("role", authority)
				.withIssuedAt(new Date())
				.withExpiresAt(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
				.sign(Algorithm.HMAC256(SECRET_KEY));
	}



	public String validateTokenAndRetrieveSubject(String jwt) {
		  return JWT.require(Algorithm.HMAC256(SECRET_KEY))
	            .build()
	            .verify(jwt)
	            .getSubject();
		
	}
	
	
	

}
