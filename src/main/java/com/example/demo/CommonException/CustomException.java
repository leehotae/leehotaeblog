package com.example.demo.CommonException;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CustomException extends Exception {

	private final HttpStatus status;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomException(String message,HttpStatus status)
	{
		super(message);
		this.status=status;
	}
	
}
