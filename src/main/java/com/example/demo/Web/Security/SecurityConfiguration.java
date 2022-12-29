package com.example.demo.Web.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;


import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


import com.example.demo.Auth.CustomUserDetails.CustomUserDetailsService;
import com.example.demo.User.UserRepository;
import com.example.demo.Web.Jwt.JwtAuthenticationFilter;
import com.example.demo.Web.Jwt.JwtAuthorizationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@RequiredArgsConstructor


public class SecurityConfiguration {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private CorsConfig corsConfig;
@Autowired
private AuthenticationConfiguration authenticationConfiguration;

	
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		JwtAuthenticationFilter jwtAuthenticationFilter=
				new JwtAuthenticationFilter(objectMapper,authenticationConfiguration.getAuthenticationManager());
		jwtAuthenticationFilter.setFilterProcessesUrl("/api/auth/login");
		http.
		addFilter(corsConfig.corsFilter()).
		csrf().disable()
		.formLogin().disable()
		.httpBasic().disable()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
		and()
		.exceptionHandling()
		.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
		.and()
		.addFilter(jwtAuthenticationFilter)
		.addFilter(new JwtAuthorizationFilter(authenticationConfiguration.getAuthenticationManager(),userRepository))
		.authorizeRequests()
		.antMatchers("/api/auth/**","/upload/**","/api/image").permitAll()
		.antMatchers(HttpMethod.GET,"/api/board/**").permitAll()
		.anyRequest()
		.authenticated();
		return http.build();
	}
	

	
	  @Bean
	  public UserDetailsService userDetailsService() {
	    return new CustomUserDetailsService();
	  }
	  
	  @Bean
	  public BCryptPasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	  }
	  
	
	

}
