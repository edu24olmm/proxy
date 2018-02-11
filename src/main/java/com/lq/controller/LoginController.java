/**
 * 
 */
package com.lq.controller;

import com.lq.interceptor.LoginInterceptor;
import com.lq.utils.EncoderHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wangxiaoran
 *
 */
@Controller
@RequestMapping("/site")
public class LoginController {

	//private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/login", method= RequestMethod.GET)
	public ModelAndView login1()throws Exception{
		ModelAndView mav = new ModelAndView("login");
		return mav;
	}

	@RequestMapping("/logout")
	public String logout(HttpServletRequest req, HttpServletResponse resp)throws Exception{
		req.getSession().removeAttribute(LoginInterceptor.LOGIN_KEY_IN_SESSION);
		return "redirect:/site/login";
	}

	/**
	 * 登录
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/login", method= RequestMethod.POST)
	public int login(HttpServletRequest req, HttpServletResponse resp, String pwd)throws Exception{
		int res;
		if("f19bd0844e53369373385609e28dbf84".equals(EncoderHandler.encodeByMD5(pwd))==false){
			res = -10;
		}else{
			res = 1;
			req.getSession().setAttribute(LoginInterceptor.LOGIN_KEY_IN_SESSION, 1);
		}
		return res;
	}
}
