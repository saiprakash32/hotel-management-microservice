package com.authservice.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.authservice.service.CustomerUserDetailsService;
import com.authservice.service.JWTService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTFilter extends OncePerRequestFilter {

	@Autowired
	private JWTService jwtService; // Service for JWT validation and extraction

	@Autowired
	private CustomerUserDetailsService userDetailsService; // Custom service to load user details from the database

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// Extract the Authorization header from the request
		String authHeader = request.getHeader("Authorization");

		// Check if the header is not null and starts with "Bearer "
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			// Remove "Bearer " prefix to get the actual token
			String jwt = authHeader.substring(7);

			// Validate the token and extract the username (subject)
			String username = jwtService.validateTokenAndRetrieveSubject(jwt);

			// Check if username is extracted and no authentication is set in the context
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				// Load user details from the database
				var userDetails = userDetailsService.loadUserByUsername(username);

				// Create authentication token with user details and authorities
				var authToken = new UsernamePasswordAuthenticationToken(userDetails, null,
						userDetails.getAuthorities());

				// Attach request details (like remote address) to the authentication token
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				// Set the authentication in the security context
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}

		// Continue the filter chain
		filterChain.doFilter(request, response);
	}
}
