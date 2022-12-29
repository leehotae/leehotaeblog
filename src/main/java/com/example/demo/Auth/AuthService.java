package com.example.demo.Auth;


import java.io.IOException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;


import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Auth.ChangePasswordToken.ChangePasswordToken;
import com.example.demo.Auth.ChangePasswordToken.ChangePasswordTokenRepository;
import com.example.demo.Auth.Dto.AccountPutDto;
import com.example.demo.Auth.Dto.ChangePasswordPutDto;

import com.example.demo.Auth.VerificationToken.VerificationToken;
import com.example.demo.Auth.VerificationToken.VerificationTokenRepository;
import com.example.demo.CommonException.CustomException;
import com.example.demo.Constants.Constants;
import com.example.demo.Image.ImageService;
import com.example.demo.User.User;
import com.example.demo.User.UserRepository;
import com.example.demo.User.Dto.UserSignRequsetDto;
import com.example.demo.Web.Host.HostEnviroment;
import com.example.demo.Web.Mail.MailContentBuilder;
import com.example.demo.Web.Mail.MailService;
import com.example.demo.Web.Mail.NotificationEmail;

import lombok.RequiredArgsConstructor;


@Service

@	RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final ImageService imageService;
	
	private final ChangePasswordTokenRepository changePasswordTokenRepository;
	
	private final VerificationTokenRepository verificationTokenRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final MailContentBuilder mailContentBuilder;
	private final MailService mailService;
	
	
	@Transactional 
	public void changePasswordPost(String email) throws CustomException
	{
		User user=userRepository.findByEmail(email).orElse(null);
		if (user==null )
		{
			throw new CustomException("잘못된접근",HttpStatus.UNAUTHORIZED);
		}
		if(user.isVerificationTokenCheck()==false)
		{
			throw new CustomException("잘못된접근",HttpStatus.UNAUTHORIZED);
		}

		String token=generateChangePasswordToken(email);
		String link =HostEnviroment.SERVER_HOST+Constants.RESET_PASSWORD_PATH+"/"+token;
		String messasge=mailContentBuilder.build(link,"resetPassword");
		mailService.sendMail(new NotificationEmail("비밀번호 재설정",email,messasge));
		
	}
	
	
	@Transactional 
	public void changePasswordPut(ChangePasswordPutDto dto) throws CustomException
	{
		

		String token=dto.getToken();
		String password=dto.getPassword();
		
		
		ChangePasswordToken cptoken=changePasswordTokenRepository.findByToken(token).orElse(null);

		if(cptoken==null)
		{
			throw new CustomException("잘못된접근",HttpStatus.UNAUTHORIZED);
		}
		
User user=userRepository.findByEmail(cptoken.getEmail()).orElse(null);

if (user==null)
{
	throw new CustomException("잘못된접근",HttpStatus.UNAUTHORIZED);
}

if(Duration.between(cptoken.getCreateTime(),LocalDateTime.now()).getSeconds()>Constants.ACTIVATION_EMAIL_EFFECTIVE_SECONDS)
{
	throw new CustomException("유효기간이 지난 토큰입니다. 비밀번호 변경 요청을 다시해주세요.",HttpStatus.UNAUTHORIZED);
}

user.setPassword(bCryptPasswordEncoder.encode(password));
		
	}
	
	
	

	
	@Transactional
	public void changeAccount(String email,AccountPutDto dto,MultipartFile image) throws IllegalStateException, IOException, CustomException
	{
		
		User user=userRepository.findByEmail(email).orElse(null);
		User user2=userRepository.findByUsername(dto.getUsername()).orElse(null);
		
		if(image!=null)
		{
			user.setProfileImageUrl(imageService.이미지저장(image));
		}

		if(!(user2==null))
		{
			if((!user2.getUsername().equals(user.getUsername())))
				
			{
				
				if(userInvalidityCheck(user2))
				{			
					userRepository.delete(user2);
					userRepository.flush();		
				}
				else
				{
					throw new CustomException("이미 존재하는 username입니다.",HttpStatus.CONFLICT);
				}
				
		}
		}
		
		user.setUsername(dto.getUsername());
		
	
		if(!(dto.getPassword()==null))
		user.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));

	}
	
	
	
	private boolean userInvalidityCheck(User user)
	{
	return 	Duration.between(user.getCreateDate(),LocalDateTime.now()).getSeconds()>Constants.ACTIVATION_EMAIL_EFFECTIVE_SECONDS && !user.isVerificationTokenCheck();
	}
	
	
	@Transactional
	public void signup(UserSignRequsetDto userdto) throws CustomException
	{
		
		User user=userRepository.findByEmail(userdto.getEmail()).orElse(null);
		User user2=userRepository.findByUsername(userdto.getUsername()).orElse(null);
		
		if (user!=null)
		{
			if (user.isVerificationTokenCheck())
				throw new CustomException("이미 회원가입 되어있습니다.",HttpStatus.CONFLICT);
			else
			{		
				if (user2==null)
				{
					user.setEmail(userdto.getEmail());
					user.setUsername(userdto.getUsername());
					user.setPassword(bCryptPasswordEncoder.encode(userdto.getPassword()));
					user.setCreateDate(LocalDateTime.now());
				}
				else
				{
					
					if(userInvalidityCheck(user2))
					{
						if (user2.getUsername().equals(user.getUsername()))
						{
							user.setPassword(bCryptPasswordEncoder.encode(userdto.getPassword()));
							user.setCreateDate(LocalDateTime.now());
						}
						else
						{
						userRepository.delete(user2);
						userRepository.flush();
						user.setUsername(userdto.getUsername());
						user.setPassword(bCryptPasswordEncoder.encode(userdto.getPassword()));
						user.setCreateDate(LocalDateTime.now());
						}
					}
					else
					{
						throw new CustomException("이미 존재하는 username입니다",HttpStatus.CONFLICT);
					}
					
				}

			}
			
			
		}
		else
		{
if(user2==null)
{
			user=new User();
		user.setEmail(userdto.getEmail());
		user.setUsername(userdto.getUsername());
		user.setPassword(bCryptPasswordEncoder.encode(userdto.getPassword()));
		user.setVerificationTokenCheck(false);
		userRepository.save(user);
		}

else
{
	
	if(userInvalidityCheck(user2))
	{
		userRepository.delete(user2);
		userRepository.flush();
		user=new User();
		user.setEmail(userdto.getEmail());
		user.setUsername(userdto.getUsername());
		user.setPassword(bCryptPasswordEncoder.encode(userdto.getPassword()));
		user.setVerificationTokenCheck(false);
		userRepository.save(user);
	}
	else
	{
		throw new CustomException("이미 존재하는 username입니다",HttpStatus.CONFLICT);
	}
	
}
		}

		String token=generateVerificationToken(user);
		String link =HostEnviroment.SERVER_HOST+Constants.ACTIVATE_EMAIL_PATH+"/"+token;
		String messasge=mailContentBuilder.build(link,"activateEmail");
		mailService.sendMail(new NotificationEmail("계정 활성화를 실행해주세요.",user.getEmail(),messasge));
	}
	@Transactional
	private String generateVerificationToken(User user)
	{
		String token=UUID.randomUUID().toString();
		VerificationToken verificationToken=verificationTokenRepository.findByEmail(user.getEmail()).orElse(null);
		
		if (verificationToken!=null)
		{
			verificationToken.setToken(token);
			verificationToken.setCreateTime(user.getCreateDate());
			return token;
		}
		verificationToken=new VerificationToken();
		verificationToken.setEmail(user.getEmail());
		verificationToken.setToken(token);
		verificationToken.setCreateTime(user.getCreateDate());
		verificationTokenRepository.save(verificationToken);
		return token;
		
	
	}
	

	
	@Transactional
	public void verifyAccount(String token) throws CustomException
	{
		Optional<VerificationToken> verificationTokenOptional=
				verificationTokenRepository.findByToken(token);
		
			verificationTokenOptional.orElseThrow(()-> new CustomException("잘못된 토큰",HttpStatus.BAD_REQUEST));
			
				fetchUserAndEnable(verificationTokenOptional.get());
			

		
			
	}
	@Transactional
	private void fetchUserAndEnable(VerificationToken verificationToken) throws CustomException {
	
		String email=verificationToken.getEmail();
		User user=userRepository.findByEmail(email).orElseThrow(()->new CustomException("유저를 찾을수 없음 "+email,HttpStatus.NOT_FOUND));
		
		System.out.println(Duration.between(user.getCreateDate(),LocalDateTime.now()).getSeconds());
		if (userInvalidityCheck(user))
		{
		
			userRepository.delete(user);
			verificationTokenRepository.delete(verificationToken);
			throw new CustomException("유효기간이 지난 토큰입니다. 회원가입을 다시 해주세요.",HttpStatus.UNAUTHORIZED);
		}

		
		user.setVerificationTokenCheck(true);
		userRepository.save(user);
		
	}
	
	
	
	@Transactional
	private String generateChangePasswordToken(String  email)
	{
		String token=UUID.randomUUID().toString();
		ChangePasswordToken changePasswordToken=changePasswordTokenRepository.findByEmail(email).orElse(null);
		
		if (changePasswordToken!=null)
		{
			changePasswordToken.setToken(token);
			changePasswordToken.setCreateTime(LocalDateTime.now());
			return token;
		}
		changePasswordToken=new ChangePasswordToken();
		changePasswordToken.setEmail(email);
		changePasswordToken.setToken(token);
		changePasswordToken.setCreateTime(LocalDateTime.now());
		changePasswordTokenRepository.save(changePasswordToken);
		return token;
		
	
	}
	
	
	
}
