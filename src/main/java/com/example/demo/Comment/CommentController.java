package com.example.demo.Comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Auth.CustomUserDetails.CustomUserDetails;
import com.example.demo.Comment.Dto.CommentDetailResponseDto;
import com.example.demo.Comment.Dto.CommentWriteRequestDto;
import com.example.demo.CommonDto.ResponseDto;
import com.example.demo.CommonException.CustomException;


@RestController
@RequestMapping("/api/board/{boardid}/comment")
public class CommentController {

	@Autowired
	private CommentService commentService;
	
	@GetMapping("")
	public ResponseEntity<?> comment(@PathVariable Long boardid)
	{

		return new ResponseEntity<>(new ResponseDto<>("코멘트보기",null),HttpStatus.OK);
	}
	
	@DeleteMapping("/{commentid}")
	public ResponseEntity<?> commentDelete(@PathVariable Long commentid,Authentication authentication) 
			throws CustomException
	{

		CustomUserDetails userDetails=(CustomUserDetails)authentication.getPrincipal();
		if(userDetails==null)
		{
			return new ResponseEntity<>(new ResponseDto<>("로그인필요",null),HttpStatus.UNAUTHORIZED);
		}
		
		commentService.코멘트삭제(commentid,userDetails.getUser().getId());
		return new ResponseEntity<>(new ResponseDto<>("삭제완료",null),HttpStatus.OK);
	}
	
	
	
	@PostMapping("")
	public ResponseEntity<?>  commentWrite(@PathVariable Long boardid,
			@RequestBody CommentWriteRequestDto dto,Authentication authentication)
	{
		CustomUserDetails userDetails=(CustomUserDetails)authentication.getPrincipal();
		if(userDetails==null)
		{
			return new ResponseEntity<>(new ResponseDto<>("로그인필요",null),HttpStatus.UNAUTHORIZED);
		}
		CommentDetailResponseDto commentDto=commentService.코멘트등록(boardid,dto.getText(),userDetails.getUser());
		if(commentDto==null)
		{
			return new ResponseEntity<>(new ResponseDto<>("코멘트등록 실패",null),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(new ResponseDto<>("코멘트등록 완료",commentDto),HttpStatus.OK);
	}
	
	
	
	
	@PutMapping("/{commentid}")
	public ResponseEntity<?>  commentUpdate(@PathVariable Long boardid,@PathVariable Long commentid,
			@RequestBody CommentWriteRequestDto dto,Authentication authentication) throws CustomException
	{
		CustomUserDetails userDetails=(CustomUserDetails)authentication.getPrincipal();
		if(userDetails==null)
		{
			return new ResponseEntity<>(new ResponseDto<>("로그인필요",null),HttpStatus.UNAUTHORIZED);
		}
		CommentDetailResponseDto commentDto=commentService.코멘트수정(boardid,commentid,dto.getText(),userDetails.getUser());
		if(commentDto==null)
		{
			return new ResponseEntity<>(new ResponseDto<>("코멘트수정 실패",null),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(new ResponseDto<>("코멘트수정 완료",commentDto),HttpStatus.OK);
	}
	
	
	
	

}
