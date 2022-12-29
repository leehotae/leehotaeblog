package com.example.demo.Comment.Dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;




import com.example.demo.Comment.Comment;
import com.example.demo.Reply.Reply;
import com.example.demo.Reply.Dto.ReplyDetailResponseDto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentDetailResponseDto
{


	public CommentDetailResponseDto(Comment comment) {
		id= comment.getId();
		boardId=comment.getBoard().getId();
		userId=comment.getUser().getId();
		userProfileImage=comment.getUserProfileImage();
		createDate=comment.getCreateDate();
		replies=new ArrayList<>();
		if(comment.getReplies()!=null)
		for (Reply reply : comment.getReplies()) {
			replies.add(new ReplyDetailResponseDto(reply));
		}
		
		text=comment.getText();
		username=comment.getUsername();

	}
	private final Long id;
	private final Long boardId;
	private final Long  userId;
	private final String userProfileImage;
	private final LocalDateTime createDate;
	private final List<ReplyDetailResponseDto> replies;

	private final String text;
	private final String username;
}