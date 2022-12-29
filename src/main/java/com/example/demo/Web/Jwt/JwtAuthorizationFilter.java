package com.example.demo.Web.Jwt;

import java.io.IOException;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.Auth.CustomUserDetails.CustomUserDetails;
import com.example.demo.User.User;
import com.example.demo.User.UserRepository;
import com.example.demo.Web.Utils.CookiesUtils;


// 인가
public class JwtAuthorizationFilter extends BasicAuthenticationFilter{
	
	private UserRepository userRepository;
	
	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
		super(authenticationManager);
		this.userRepository = userRepository;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String header = request.getHeader(AccessTokenProperties.HEADER_STRING);
		String accessToken=null;
		String refreshToken=null;
		
		Cookie cookies[]=request.getCookies();
		

		
		DecodedJWT accessJWT=null;

		

		if(cookies==null || request.getRequestURI().equals(RefreshTokenProperties.REFRESH_TOKEN_URI))
		{	
			chain.doFilter(request, response);
            return;
		}
		
		
		refreshToken=CookiesUtils.cookiesSelect(cookies, RefreshTokenProperties.COOKIE_STRING);
		
		if(refreshToken==null ||header == null || !header.startsWith(AccessTokenProperties.TOKEN_PREFIX)) {

			chain.doFilter(request, response);
            return;
		}
		else
		{
			accessToken = request.getHeader(AccessTokenProperties.HEADER_STRING);
					
			if(accessToken==null)
			{
				chain.doFilter(request, response);
	            return;
			}

			accessToken=accessToken.replace(AccessTokenProperties.TOKEN_PREFIX, "");			
			
			try
			{
				JWT.require(Algorithm.HMAC512(RefreshTokenProperties.SECRET)).build().verify(refreshToken);
			}
			catch(Exception e)
			{		
					request.setAttribute("error", "error_refreshToken");
					chain.doFilter(request, response);
           			return;
			}
			try
			{
			
			accessJWT=	JWT.require(Algorithm.HMAC512(AccessTokenProperties.SECRET)).build().verify(accessToken);			
			}
			catch(Exception e)
			{	
				if(e.getClass()==TokenExpiredException.class)
				{

					request.setAttribute("error", "expired_accessToken");
				}
			
				chain.doFilter(request, response);
           		return;	
			}
		}

		// 토큰 검증 (이게 인증이기 때문에 AuthenticationManager도 필요 없음)
		// 내가 SecurityContext에 집적접근해서 세션을 만들때 자동으로 UserDetailsService에 있는 loadByUsername이 호출됨.
		
		String email = accessJWT
				.getClaim("email").asString();
		
		if(email != null) {	
			User user = userRepository.findByEmail(email).orElse(null);
			
			if(user!=null)
			{
			CustomUserDetails principalDetails = new CustomUserDetails(user);
			Authentication authentication =
					new UsernamePasswordAuthenticationToken(
							principalDetails, //나중에 컨트롤러에서 DI해서 쓸 때 사용하기 편함.
							null, // 패스워드는 모르니까 null 처리, 어차피 지금 인증하는게 아니니까!!
							principalDetails.getAuthorities());
			
			// 강제로 시큐리티의 세션에 접근하여 값 저장
			SecurityContextHolder.getContext().setAuthentication(authentication);
			}
			
		}

		chain.doFilter(request, response);
	}
	
}