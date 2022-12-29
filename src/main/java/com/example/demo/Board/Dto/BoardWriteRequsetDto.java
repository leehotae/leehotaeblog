package com.example.demo.Board.Dto;

import javax.validation.constraints.NotBlank;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BoardWriteRequsetDto {

	@NotBlank(message="제목은 공백일수 없습니다.")
	private final String title;
	@NotBlank(message="내용은 공백일수 없습니다.")
	private final String text;
	
}
