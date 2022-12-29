package com.example.demo.User.Dto;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.example.demo.User.Validation.ValidationGroups;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserSignRequsetDto {

	@NotBlank(message="이메일은 필수 입력 값입니다.",
			groups = ValidationGroups.EmailNotEmptyGroup.class)
	@Email(message="이메일 형식에 맞지 않습니다.")
	private  final String email;
	
	private  final String username;

	@NotBlank(message="비밀번호는 필수 입력 값입니다.",
			groups = ValidationGroups.PasswordNotEmptyGroup.class)
	@Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
	message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.",
	groups = ValidationGroups.PasswordPatternCheckGroup.class)
	private  final String password;
	
}
