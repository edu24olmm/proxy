package com.lq.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component(value="loginInterceptor")
public class LoginInterceptor extends HandlerInterceptorAdapter {

	public final static String LOGIN_KEY_IN_SESSION = "_login_user_info_key";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
//		String contextPath = request.getContextPath();
//		if(contextPath!=null && contextPath.length()>1){
//			request.setAttribute("contextPath", contextPath);
//		}
		String uri = request.getRequestURI();
//		if(StringUtils.lastIndexOf(uri, '/')==(uri.length()-1)){
			uri = StringUtils.substring(uri, 0, uri.length()-1);
//		}
		boolean passed;
		if(StringUtils.startsWith(uri, "/site/") || StringUtils.startsWith(uri, "/error")){
			passed = true;
		}else{
			passed = request.getSession().getAttribute(LOGIN_KEY_IN_SESSION)!=null;
			if(passed==false){
				if(StringUtils.equalsIgnoreCase(request.getHeader("X-Requested-With"), "XMLHttpRequest")){
					response.setStatus(1000);
				}else{
					response.sendRedirect("/site/login");
				}
			}
		}
		return passed;
	}

}
