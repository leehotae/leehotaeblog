package com.example.demo.Auth.Dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;




@Getter
@RequiredArgsConstructor
public class AccountGetDto {

	private final String email;
	private final String username;
	private final String imageUrl;
}
