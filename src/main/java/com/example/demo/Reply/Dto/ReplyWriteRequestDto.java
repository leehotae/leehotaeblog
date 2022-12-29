package com.example.demo.Reply.Dto;


import lombok.Getter;

import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReplyWriteRequestDto {
	
	private final String toUsername;
	private final Long toUserId;
	private final String text;
}
