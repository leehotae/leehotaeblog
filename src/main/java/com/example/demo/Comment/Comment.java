package com.example.demo.Comment;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.CreationTimestamp;

import com.example.demo.Board.Board;
import com.example.demo.Reply.Reply;
import com.example.demo.User.User;
import com.fasterxml.jackson.annotation.JsonIgnore;


import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="board_id")
	@JsonIgnore
	private Board board;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;
	private String userProfileImage;
	@CreationTimestamp
	private LocalDateTime createDate;
	@OneToMany (mappedBy = "comment",orphanRemoval = true, fetch=FetchType.LAZY)
	@javax.persistence.OrderBy("id")
	private List<Reply> replies;
	@NotBlank
	private String text;
	private String username;
}
