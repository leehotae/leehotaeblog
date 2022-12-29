package com.example.demo.Web.Mail;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class NotificationEmail {

	
	private final String subject;
	private final String recipient;
	private final String body;
}
