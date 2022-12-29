package com.example.demo.Reply;

import java.time.LocalDateTime;


import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.CreationTimestamp;


import com.example.demo.Comment.Comment;
import com.example.demo.User.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Reply {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="comment_id")
	@JsonIgnore
	private Comment comment;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;
	@CreationTimestamp
	private LocalDateTime createDate;
	@NotBlank
	private String text;
	private String username;
	private String toUsername;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="touser_id")
	private User toUser;
	private String userProfileImage;
}
