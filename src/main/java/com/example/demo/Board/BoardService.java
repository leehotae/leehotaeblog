package com.example.demo.Board;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Board.Dto.BoardDetailResponseDto;
import com.example.demo.Board.Dto.BoardWriteRequsetDto;
import com.example.demo.CommonException.CustomException;
import com.example.demo.User.User;



@Service

public class BoardService {

	@Autowired
private BoardRepository boardRepository;

	@Transactional
	public Long 글등록하기(User user,BoardWriteRequsetDto dto)
	{

		Board board=boardRepository.save(new Board(user,dto.getTitle(),dto.getText()));
		return board.getId();
	}
	
	@Transactional
	public BoardDetailResponseDto 글상세보기(Long id)
	{
	Board board=boardRepository.findById(id).orElse(null);
	
	if(board==null)
	{
		return null;
	}
	board.setViews(board.getViews()+1);
	return new BoardDetailResponseDto(board);
	}
	
	@Transactional
	public void 글삭제하기(Long boardId, Long userId) throws CustomException
	{

		Board board=boardRepository.findById(boardId).orElse(null);
		if(board==null)
		{
			throw new CustomException("존재하지 않는 게시글", HttpStatus.NO_CONTENT);
		}
		else if(board.getUser().getId()==userId)
		{
			boardRepository.deleteById(boardId);
		}
		else
		{
			throw new CustomException("삭제 권한 없음",HttpStatus.FORBIDDEN);
		}
	}
	
	
	@Transactional
	public void 글수정하기(Long boardId, Long userId,BoardWriteRequsetDto updateBoard) 
			throws CustomException
	{
		Board board=boardRepository.findById(boardId).orElse(null);
		if(board==null)
		{
			throw new CustomException("존재하지 않는 게시글", HttpStatus.NO_CONTENT);
		}
		else if(board.getUser().getId()==userId)
		{
			board.setCreateDate(LocalDateTime.now());
			board.setTitle(updateBoard.getTitle());
			board.setText(updateBoard.getText());
			board.setUserProfileImage(board.getUser().getProfileImageUrl());
			board.setUsername(board.getUser().getUsername());
		}
		else
		{
			throw new CustomException("수정 권한 없음", HttpStatus.FORBIDDEN);
		}
	}
	
	
	public 	Page<BoardDetailResponseDto> 글목록(Integer page)
	{  Pageable pageable=PageRequest.of(page, 8,Sort.by("id").descending());
	
	Page<Board> boardList=boardRepository.findAll(pageable);
	Page<BoardDetailResponseDto> dtoList=boardList.map(board->new BoardDetailResponseDto(board));
	return dtoList;
	}
	
	
	public Page<BoardDetailResponseDto>  글목록_제목(String title,		Integer page)
	{
		Pageable pageable=PageRequest.of(page, 8,Sort.by("id").descending());
		Page<Board> boardList=boardRepository.findByTitle(title,pageable);
		Page<BoardDetailResponseDto> dtoList=boardList.map(board->new BoardDetailResponseDto(board));
	return dtoList;
		
	}
	
}
