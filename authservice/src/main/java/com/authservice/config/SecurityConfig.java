package com.authservice.config;

import com.authservice.service.CustomerUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;




@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private CustomerUserDetailsService customerUserDeatlsService;
	@Autowired
	private JWTFilter jwtFilter;

	
	@Bean
	public PasswordEncoder getEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
		return  config.getAuthenticationManager();
	}
	
	@Bean
	public AuthenticationProvider authProvider() {
		
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(customerUserDeatlsService);
		authProvider.setPasswordEncoder(getEncoder());
		return authProvider;
		
	}
	
	@Bean
	public SecurityFilterChain securityFilterchain(HttpSecurity http) throws Exception {
		
		
		http.csrf(csrf->csrf.disable());
		
		http.authorizeHttpRequests(auth->auth
						.requestMatchers(
								"/api/v1/auth/register",
								"/api/v1/auth/login",
								"/v3/api-docs/**",
								"/swagger-ui/**",
								"/swagger-ui.html", 
								"/swagger-resources/**", 
								"/webjars/**")
						.permitAll()
						.requestMatchers("/api/v1/admin/welcome").hasAnyRole("ADMIN")
						.anyRequest().authenticated());
		 // Register the authentication provider
        http.authenticationProvider(authProvider());

        // Add your JWTFilter before the UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
		
	}

}
