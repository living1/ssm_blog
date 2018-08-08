package com.java1234.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.java1234.entity.Blogger;
import com.java1234.service.BloggerService;
import com.java1234.util.CryptographyUtil;

/**
 * 博主controller层
 * @author living
 *
 */
@Controller
@RequestMapping("/blogger")
public class BloggerController {

	@Resource
	private BloggerService bloggerService;
	
	@RequestMapping("/login")
	public String login(Blogger blogger, HttpServletRequest request) {
		Subject subject=SecurityUtils.getSubject();//获取当前登录的用户
		//封装一个token，存入用户名和密码
		UsernamePasswordToken token = new UsernamePasswordToken(blogger.getUserName(), CryptographyUtil.md5(blogger.getPassword(), "java1234"));
		try {
			//这个可能会出现运行时异常
			subject.login(token);//登录验证,最终调用的就是myrealm中的doGetAuthenticationInfo方法
			return "redirect:/admin/main.jsp";
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			request.setAttribute("blogger", blogger);//将用户输入的错误信息回显到页面上，不然每次错了就要全部重新输入，用户体验差
			request.setAttribute("errorInfo", "用户名或者密码错误!");
			return "login";
		}
	}
	
	//关于博主
	@RequestMapping("/aboutMe")
	public ModelAndView aboutMe()throws Exception{//model是数据，view是转发的视图
		ModelAndView mav=new ModelAndView();
		mav.addObject("mainPage","foreground/blogger/info.jsp");
		mav.addObject("pageTitle", "关于博主_java开源博客系统");
		mav.setViewName("mainTemp");
		return mav;
	}
	
}
