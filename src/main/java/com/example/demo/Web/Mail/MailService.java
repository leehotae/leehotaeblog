package com.example.demo.Web.Mail;

import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.demo.CommonException.CustomException;
import com.example.demo.Web.Host.HostEnviroment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class MailService {

	private final JavaMailSender mailSender;
	
	@Async
	public void sendMail(NotificationEmail notificationEmail) throws CustomException
	{
		MimeMessagePreparator messagePreparator=
				mimeMessage->
		{
			MimeMessageHelper messageHelper=new MimeMessageHelper(mimeMessage, true, "UTF-8");
            messageHelper.setFrom(HostEnviroment.EMAIL_ADDRESS);
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(notificationEmail.getBody(), true);
		};
            try {
                mailSender.send(messagePreparator);
                log.info("활성화 메일이 보내졌다");
            } catch (MailException e) {
                log.error(String.valueOf(e));
                throw new CustomException("메일을 여기로 보내는 중 에러 발생 :  " + notificationEmail.getRecipient(),HttpStatus.INTERNAL_SERVER_ERROR);
            }
		}
		
}

	

