package com.example.demo.User.Dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserSignResponseDto {
	
	private final Long id;

	private final String email;
	
	private final String profileImage;
}
