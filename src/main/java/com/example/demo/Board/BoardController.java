package com.example.demo.Board;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Auth.CustomUserDetails.CustomUserDetails;
import com.example.demo.Board.Dto.BoardDetailResponseDto;
import com.example.demo.Board.Dto.BoardWriteRequsetDto;
import com.example.demo.CommonDto.ResponseDto;
import com.example.demo.CommonException.CustomException;

@RestController

@RequestMapping("/api/board")
public class BoardController {

	@Autowired
	private BoardService boardService;
	
	@GetMapping("")
	public ResponseEntity<?> boards(@RequestParam(required=false) String title,
			@RequestParam("page")Integer page)
	{
		if (title==null ||title=="")
		{
		Page<BoardDetailResponseDto> dtoList= boardService.글목록(page);
		return new ResponseEntity<>(new ResponseDto<>("글목록",dtoList),HttpStatus.OK);
		}
		else
		{
			Page<BoardDetailResponseDto> dtoList=boardService.글목록_제목(title,page);
			return new ResponseEntity<>(new ResponseDto<>("글목록_"+title,dtoList),HttpStatus.OK);
		}
	}
	

	@GetMapping("/{id}")
	public ResponseEntity<?> board(@PathVariable Long id)
	{
		BoardDetailResponseDto dto= boardService.글상세보기(id);
		return new ResponseEntity<>(new ResponseDto<>("글상세보기",dto),HttpStatus.OK);
	}

	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> boardDelete(@PathVariable Long id, Authentication authentication) 
			throws CustomException
	{
		
		CustomUserDetails userDetails=(CustomUserDetails)authentication.getPrincipal();
		if(userDetails==null)
		{
			return new ResponseEntity<>(new ResponseDto<>("로그인필요",null),
					HttpStatus.UNAUTHORIZED);
		}
	 boardService.글삭제하기(id,userDetails.getUser().getId());
	return new ResponseEntity<>(new ResponseDto<>("글삭제성공",null),HttpStatus.OK);
	}
	
	
	
	@PutMapping("/{id}")
	public ResponseEntity<?> boardUpdate(@PathVariable Long id, Authentication authentication,
			@RequestBody BoardWriteRequsetDto board) throws CustomException
	{
		CustomUserDetails userDetails=(CustomUserDetails)authentication.getPrincipal();
		if(userDetails==null)
		{
			return new ResponseEntity<>(new ResponseDto<>("로그인필요",null),
					HttpStatus.UNAUTHORIZED);
			
		}
		boardService.글수정하기(id,userDetails.getUser().getId(),board);
		return new ResponseEntity<>(new ResponseDto<>("글수정성공",id),HttpStatus.OK);
	}
	
	
	@PostMapping("")
	public ResponseEntity<?>  boardWrite(@RequestBody @Validated BoardWriteRequsetDto dto, 
			Authentication authentication)
	{
		CustomUserDetails userDetails=(CustomUserDetails)authentication.getPrincipal();
		if(userDetails==null)
		{
			return new ResponseEntity<>(new ResponseDto<>("로그인필요",null),
					HttpStatus.UNAUTHORIZED);
			
		}
		Long  boardId=boardService.글등록하기(userDetails.getUser(),dto);
		return new ResponseEntity<>(new ResponseDto<>("글등록 완료",boardId),HttpStatus.OK);
	}
	
	
	
	
	
	
	
}
