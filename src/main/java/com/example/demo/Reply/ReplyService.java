package com.example.demo.Reply;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Comment.Comment;
import com.example.demo.CommonException.CustomException;
import com.example.demo.Reply.Dto.ReplyDetailResponseDto;
import com.example.demo.Reply.Dto.ReplyWriteRequestDto;
import com.example.demo.User.User;

@Service
public class ReplyService {

	@Autowired
	private ReplyRepository replyRepository;
	
	
	@Transactional
	public ReplyDetailResponseDto 대댓글등록(Long commentid, ReplyWriteRequestDto dto, User writeuser)
	{
		Comment comment=new Comment();
		Reply reply=new Reply();
		comment.setId(commentid);
		reply.setUser(writeuser);
		reply.setUsername(writeuser.getUsername());
		reply.setText(dto.getText());
		reply.setToUsername(dto.getToUsername());
		reply.setComment(comment);
		User toUser=new User();
		toUser.setId(dto.getToUserId());
		reply.setToUser(toUser);
		reply.setUserProfileImage(writeuser.getProfileImageUrl());
		reply=replyRepository.save(reply);
		return new ReplyDetailResponseDto(reply);
	}
	
	
	@Transactional
	public ReplyDetailResponseDto 대댓글수정(Long replyid, ReplyWriteRequestDto dto, User writeuser)
			throws CustomException
	{
		Reply reply=replyRepository.findById(replyid).orElse(null);
		if (reply==null)
		{
			throw new CustomException("존재하지 않는 코멘트", HttpStatus.NO_CONTENT);
		}
		
		if(reply.getUser().getId()!=writeuser.getId())
		{
			throw new CustomException("수정 권한 없음", HttpStatus.FORBIDDEN);
		}
		reply.setUsername(writeuser.getUsername());
		reply.setUserProfileImage(writeuser.getProfileImageUrl());
		reply.setText(dto.getText());
		reply.setCreateDate(LocalDateTime.now());
		return new ReplyDetailResponseDto( reply);
	}

	@Transactional
	public void 대댓글삭제(Long replyid,Long userId) throws CustomException
	{
		Reply reply=replyRepository.findById(replyid).orElse(null);
		if(reply==null)
		{
			throw new CustomException("존재하지 않는 코멘트", HttpStatus.NO_CONTENT);
		}
		else if(reply.getUser().getId()==userId)
		{
			replyRepository.deleteById(replyid);
		}
		else
		{
			throw new CustomException("삭제 권한 없음",HttpStatus.FORBIDDEN);
		}
	}

}
