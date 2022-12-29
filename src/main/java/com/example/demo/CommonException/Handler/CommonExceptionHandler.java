package com.example.demo.CommonException.Handler;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.example.demo.CommonException.CustomException;
import com.example.demo.CommonException.ErrorResponse;




@RestControllerAdvice

public class CommonExceptionHandler
{   

	
	private final String MaxUploadSize;

	
	
	public CommonExceptionHandler(@Value(	"${spring.servlet.multipart.max-file-size}") String value)
	{
		MaxUploadSize=value;
	}
	
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
          ErrorResponse errorResponse
                = ErrorResponse
                .create()
                .status(HttpStatus.BAD_REQUEST)
                .message("유효하지않은 입력요청입니다.")
                .errors(e);
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
	
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
  
        ErrorResponse errorResponse
        = ErrorResponse
        .create()
        .status(e.getStatus())
        .message(e.getMessage());
        return new ResponseEntity<>(errorResponse, e.getStatus());
  
    }	
    
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    
    protected ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
    	  
        ErrorResponse errorResponse
        = ErrorResponse
        .create()
        .status(HttpStatus.BAD_REQUEST)
        .message("이미지 용량 초과 최대용량:"+MaxUploadSize);
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
  
    }	
    
}
	
    
    
    


	
    


