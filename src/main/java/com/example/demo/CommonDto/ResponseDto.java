
package com.example.demo.CommonDto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ResponseDto<T> {

	
	private final String message;
	private final T data;
	
}
