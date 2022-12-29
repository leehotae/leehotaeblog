package com.example.demo.Web.Jwt;

public interface RefreshTokenProperties {
	String SECRET = "lhtsecretkey_refresh";
	int EXPIRATION_TIME = 60*1000*60*24; 
	String TOKEN_PREFIX = "Bearer ";
	String HEADER_STRING = "RefreshToken";
	String COOKIE_STRING="refreshToken";
	String REFRESH_TOKEN_URI="/api/auth/refreshToken";
}
