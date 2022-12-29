package com.example.demo.Auth.Dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.example.demo.User.Validation.ValidationGroups;
import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter

@RequiredArgsConstructor
public class ChangePasswordPostDto {
	
	@NotBlank(message="이메일은 필수 입력 값입니다.",groups = ValidationGroups.EmailNotEmptyGroup.class)
	@Email(message="이메일 형식에 맞지 않습니다.")
	private  String email;
	

}
