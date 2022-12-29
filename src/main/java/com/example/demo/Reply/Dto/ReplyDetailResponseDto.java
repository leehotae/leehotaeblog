package com.example.demo.Reply.Dto;

import java.time.LocalDateTime;


import com.example.demo.Reply.Reply;

import lombok.Getter;



@Getter
public class ReplyDetailResponseDto {


	public ReplyDetailResponseDto(Reply reply) {
		
		id=reply.getId();
		commentId=reply.getComment().getId();
		userId=reply.getUser().getId();
		
		createDate=reply.getCreateDate();
		text=reply.getText();
		username=reply.getUsername();
		toUsername=reply.getToUsername();
		toUserId=reply.getToUser().getId();
		userProfileImage=reply.getUserProfileImage();
		
	}
	private final Long id;
	private final Long commentId;
	private final Long userId;

	private final LocalDateTime createDate;
	private final String text;
	private final String username;
	
	private final String toUsername;
	
	private final Long toUserId;

	private final String userProfileImage;
	
}
