package com.example.demo.Comment;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Board.Board;
import com.example.demo.Comment.Dto.CommentDetailResponseDto;
import com.example.demo.CommonException.CustomException;
import com.example.demo.User.User;

@Service
public class CommentService {

	@Autowired
	private CommentRepository commentRepository;
	

	
	
	
	
	@Transactional
	public CommentDetailResponseDto 코멘트등록(Long boardId, String text, User user)
	{
		Comment comment=new Comment();
		Board board=new Board();
		board.setId(boardId);
		comment.setBoard(board);
		comment.setUser(user);
		comment.setUsername(user.getUsername());
		comment.setUserProfileImage(user.getProfileImageUrl());
		comment.setText(text);
		comment=commentRepository.save(comment);
		return new CommentDetailResponseDto(comment);
		
	}
	
	
	
	@Transactional
	public CommentDetailResponseDto 코멘트수정(Long boardId,Long commentId, String text, User user)
			throws CustomException
	{
		Comment comment = commentRepository.findById(commentId).orElse(null);
		if (comment==null)
		{
			throw new CustomException("존재하지 않는 코멘트", HttpStatus.NO_CONTENT);
		}
		
		if(comment.getUser().getId()!=user.getId())
		{
			throw new CustomException("수정 권한 없음", HttpStatus.FORBIDDEN);
		}
		comment.setUsername(user.getUsername());
		comment.setUserProfileImage(user.getProfileImageUrl());
		comment.setText(text);
		comment.setCreateDate(LocalDateTime.now());
		return new CommentDetailResponseDto(comment);
	}
	
	

	
	@Transactional
	public void 코멘트삭제(Long commentId,Long userId) throws CustomException
	{
		Comment comment=commentRepository.findById(commentId).orElse(null);
		if(comment==null)
		{
			throw new CustomException("존재하지 않는 코멘트", HttpStatus.NO_CONTENT);
		}
		else if(comment.getUser().getId()==userId)
		{
			commentRepository.deleteById(commentId);
		}
		else
		{
			throw new CustomException("삭제 권한 없음",HttpStatus.FORBIDDEN);
		}
	}
	
}
