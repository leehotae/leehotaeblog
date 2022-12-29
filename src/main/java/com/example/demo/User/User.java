package com.example.demo.User;

import java.time.LocalDateTime;


import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


import org.hibernate.annotations.CreationTimestamp;



import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;

@Entity
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long  id;
	@NotBlank(message="이메일은 필수 입력 값입니다.")
	@Email(message="이메일 형식에 맞지 않습니다.")
	@Column(unique = true)
	private String email;
	private String profileImageUrl;
	@JsonIgnore
	@NotBlank(message="비밀번호는 필수 입력 값입니다.")
	private String password;
	
	@NotBlank(message="유저이름은 필수 입력 값입니다.")
	@Column(unique = true)
	private String username;
	
	@JsonIgnore
	private boolean VerificationTokenCheck;

	@CreationTimestamp
	@JsonIgnore
	private LocalDateTime createDate;
}
