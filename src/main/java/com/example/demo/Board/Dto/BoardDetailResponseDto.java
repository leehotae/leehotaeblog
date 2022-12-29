package com.example.demo.Board.Dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.Board.Board;
import com.example.demo.Comment.Comment;
import com.example.demo.Comment.Dto.CommentDetailResponseDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class BoardDetailResponseDto {

	private final Long id;
	

	private final Long userId;
	

	private final   String title;
	

	private final String text;
	
	private final String username;

	private final String userProfileImage;
	private final List<CommentDetailResponseDto> comments;
	
	
	private final LocalDateTime createDate;
	
	
	private final Long views;
	
	
	public BoardDetailResponseDto(Board board)
	{
		id=board.getId();

		userId=board.getUser().getId();
		title=board.getTitle();
		text=board.getText();
		username=board.getUsername();
		userProfileImage=board.getUserProfileImage();

		comments=new ArrayList<>();
		
		if(board.getComments()!=null)
		for (Comment comment : board.getComments()) {
			comments.add(new CommentDetailResponseDto(comment) );
		}
		
		createDate=board.getCreateDate();
		views=board.getViews();
	}
	
}
