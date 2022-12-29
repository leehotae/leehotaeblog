package com.example.demo.Web.Jwt;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.http.ResponseCookie;

import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;


import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.Auth.CustomUserDetails.CustomUserDetails;
import com.example.demo.CommonDto.ResponseDto;
import com.example.demo.User.User;
import com.example.demo.User.Dto.UserSignRequsetDto;
import com.example.demo.User.Dto.UserSignResponseDto;
import com.example.demo.Web.Host.HostEnviroment;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    private final ObjectMapper objectMapper;

	private final AuthenticationManager authenticationManager;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		UserSignRequsetDto loginRequestDto = null;
		
	
				try {
					loginRequestDto =objectMapper.readValue(request.getInputStream(), UserSignRequsetDto.class);
				} catch (IOException e) {
				
				}
			

		UsernamePasswordAuthenticationToken authenticationToken = 
				new UsernamePasswordAuthenticationToken(
						loginRequestDto.getEmail(), 
						loginRequestDto.getPassword());

		
	
		Authentication authentication = 
				authenticationManager.authenticate(authenticationToken);
		

		return authentication;
	}

	// JWT Token 생성해서 response에 담아주기
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		CustomUserDetails principalDetails = (CustomUserDetails) authResult.getPrincipal();
		

		String RefreshToken=JWT.create()
				.withSubject(principalDetails.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis()+RefreshTokenProperties.EXPIRATION_TIME))
				.withClaim("id",principalDetails.getUser().getId())
				.withClaim("email",principalDetails.getUser().getEmail())
				.sign(Algorithm.HMAC512(RefreshTokenProperties.SECRET));

      ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", RefreshToken)
                .path("/")
                .secure(false)
                .sameSite("Strict")
               .httpOnly(true)
               .maxAge(-1)
               .build();
      

      ResponseCookie loginCookie = ResponseCookie.from("login", "1")
                .path("/")
                .secure(false)
                .sameSite("Strict")
               .maxAge(-1)
               .build();

		response.addHeader("Set-Cookie", refreshCookie.toString());
		

		response.addHeader("Set-Cookie", loginCookie.toString());
		
		String AccessToken=JWT.create()
				.withSubject(principalDetails.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis()+AccessTokenProperties.EXPIRATION_TIME))
				.withClaim("id",principalDetails.getUser().getId())
				.withClaim("email",principalDetails.getUser().getEmail())
				.sign(Algorithm.HMAC512(AccessTokenProperties.SECRET));
		
		
		response.addHeader(AccessTokenProperties.HEADER_STRING, AccessTokenProperties.TOKEN_PREFIX+AccessToken);

		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		User user=principalDetails.getUser();
		ResponseDto<UserSignResponseDto> rd= new ResponseDto<UserSignResponseDto>("성공",new UserSignResponseDto(user.getId(),user.getEmail(),HostEnviroment.SERVER_HOST+"/upload/"+user.getProfileImageUrl()));

		String json =new Gson().toJson(rd);
		System.out.println(json);
		PrintWriter writer=response.getWriter();
		writer.write(json);
		writer.flush();		
	}
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {


		
		
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		ResponseDto<?> rd= new ResponseDto<>("유효하지 않은 이메일이거나 패스워드입니다.",null);
		
		String json =new Gson().toJson(rd);
	
		PrintWriter writer=response.getWriter();
		writer.write(json);
		writer.flush();		
	}


 }
