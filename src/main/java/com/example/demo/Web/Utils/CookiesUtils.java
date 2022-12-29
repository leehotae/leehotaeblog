package com.example.demo.Web.Utils;

import javax.servlet.http.Cookie;

public class CookiesUtils {

	public static String cookiesSelect(Cookie[] cookies,String string)
	{
	for (Cookie cookie : cookies) {
		
		if (cookie.getName().equals(string))
		{
			return cookie.getValue();
		}
	}
	return null;
	}
	

}
