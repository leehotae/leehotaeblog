package com.example.demo.Web.Mail;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MailContentBuilder {
	
	private final TemplateEngine templateEngine;
	
	public String build(String message,String template)
	{
		Context context=new Context();
		context.setVariable("link",message);
		return templateEngine.process(template, context);
	}
	

}
