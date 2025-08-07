package com.example.BookMyNest.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.BookMyNest.exception.CustomAccessDenialHandler;
import com.example.BookMyNest.exception.CustomAuthenticationEntryPoint;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityFilter {
	
	private final AuthFilter authFilter;
	private final CustomAccessDenialHandler customAccessDenialHandler;
	private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{ 
		httpSecurity.csrf(AbstractHttpConfigurer::disable)
		.exceptionHandling(exception -> exception.accessDeniedHandler(customAccessDenialHandler)
		.authenticationEntryPoint(customAuthenticationEntryPoint))
		.authorizeHttpRequests(request -> request
				.requestMatchers("/api/auth**", "/api/rooms/**","api/bookings/**").permitAll()
				.anyRequest().authenticated()
				)
		.sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
		return httpSecurity.build();
		
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
		return authenticationConfiguration.getAuthenticationManager();
	}
	

}
