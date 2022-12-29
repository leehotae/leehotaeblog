package com.example.demo.Reply;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Auth.CustomUserDetails.CustomUserDetails;
import com.example.demo.CommonDto.ResponseDto;
import com.example.demo.CommonException.CustomException;
import com.example.demo.Reply.Dto.ReplyDetailResponseDto;
import com.example.demo.Reply.Dto.ReplyWriteRequestDto;


@RestController
@RequestMapping("/api/board/{boardid}/comment/{commentid}/reply")
public class ReplyController {

	@Autowired
	private ReplyService replyService;
	
	@PostMapping("")
	public ResponseEntity<?>  replyWrite(@PathVariable Long commentid,
			@RequestBody ReplyWriteRequestDto dto,Authentication authentication)
	{
		CustomUserDetails userDetails=(CustomUserDetails)authentication.getPrincipal();
		if(userDetails==null)
		{
			return new ResponseEntity<>(new ResponseDto<>("로그인필요",null),HttpStatus.UNAUTHORIZED);
		}
		ReplyDetailResponseDto replyDto=replyService.대댓글등록(commentid,dto,userDetails.getUser());
		return new ResponseEntity<>(new ResponseDto<>("대댓글등록 완료",replyDto),HttpStatus.OK);
	}
	
	
	
	@PutMapping("/{replyid}")
	public ResponseEntity<?>  replyUpdate(@PathVariable Long replyid,
			@RequestBody ReplyWriteRequestDto dto,Authentication authentication) throws CustomException
	{
		CustomUserDetails userDetails=(CustomUserDetails)authentication.getPrincipal();
		
		if(userDetails==null)
		{
			return new ResponseEntity<>(new ResponseDto<>("로그인필요",null),HttpStatus.UNAUTHORIZED);
		}
		ReplyDetailResponseDto replyDto =replyService.대댓글수정(replyid,dto,userDetails.getUser());
		return new ResponseEntity<>(new ResponseDto<>("대댓글수정 완료",replyDto),HttpStatus.OK);
	}
	
	
	@DeleteMapping("/{replyid}")
	public ResponseEntity<?> replyDelete(@PathVariable Long replyid,Authentication authentication)
			throws CustomException
	{
		CustomUserDetails userDetails=(CustomUserDetails)authentication.getPrincipal();
		if(userDetails==null)
		{
			return new ResponseEntity<>(new ResponseDto<>("로그인필요",null),HttpStatus.UNAUTHORIZED);
		}
		replyService.대댓글삭제(replyid,userDetails.getUser().getId());
		return new ResponseEntity<>(new ResponseDto<>("삭제완료",null),HttpStatus.OK);
	}
	
	
	
	
}
