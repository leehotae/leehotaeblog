package com.example.demo.Web.Security;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseCookie;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			org.springframework.security.core.AuthenticationException authException)
			throws IOException, ServletException {
	
	log.info("entry point");
	if(request.getAttribute("error")!=null)
	{
		String error=request.getAttribute("error").toString();
		if (error.equals("expired_accessToken"))
		{
			log.info(error);
			errorResponseWriter(response,HttpServletResponse.SC_UNAUTHORIZED,"ExpiredTokenError");
			return;
		}

	}
	log.info("ReLogin");
	
	  ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", "")
              .path("/")
              .secure(false)
              .sameSite("Strict")
             .httpOnly(true)
             .maxAge(0)
             .build();
    ResponseCookie loginCookie = ResponseCookie.from("login", "")
              .path("/")
              .secure(false)
              .sameSite("Strict")
             .maxAge(0)
             .build();

		response.addHeader("Set-Cookie", refreshCookie.toString());
		response.addHeader("Set-Cookie", loginCookie.toString());
	errorResponseWriter(response,HttpServletResponse.SC_UNAUTHORIZED,"ReLogin");
	return;
	
	}

private void errorResponseWriter(HttpServletResponse response,int status ,String message) throws IOException
{
	
	response.setContentType("application/json;charset=UTF-8");
    response.setStatus(status);

    JsonObject responseJson = new JsonObject();
    responseJson.addProperty("message", message);

    response.getWriter().print(responseJson);
}


}
