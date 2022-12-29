package com.example.demo.Web.Jwt;

public interface AccessTokenProperties {
	String SECRET = "access_lhtsecretkey";
	int EXPIRATION_TIME = 60*1000*10; 
	String TOKEN_PREFIX = "Bearer ";
	String HEADER_STRING = "AccessToken";

}
