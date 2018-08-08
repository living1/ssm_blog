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
 * ����controller��
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
		Subject subject=SecurityUtils.getSubject();//��ȡ��ǰ��¼���û�
		//��װһ��token�������û���������
		UsernamePasswordToken token = new UsernamePasswordToken(blogger.getUserName(), CryptographyUtil.md5(blogger.getPassword(), "java1234"));
		try {
			//������ܻ��������ʱ�쳣
			subject.login(token);//��¼��֤,���յ��õľ���myrealm�е�doGetAuthenticationInfo����
			return "redirect:/admin/main.jsp";
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			request.setAttribute("blogger", blogger);//���û�����Ĵ�����Ϣ���Ե�ҳ���ϣ���Ȼÿ�δ��˾�Ҫȫ���������룬�û������
			request.setAttribute("errorInfo", "�û��������������!");
			return "login";
		}
	}
	
	//���ڲ���
	@RequestMapping("/aboutMe")
	public ModelAndView aboutMe()throws Exception{//model�����ݣ�view��ת������ͼ
		ModelAndView mav=new ModelAndView();
		mav.addObject("mainPage","foreground/blogger/info.jsp");
		mav.addObject("pageTitle", "���ڲ���_java��Դ����ϵͳ");
		mav.setViewName("mainTemp");
		return mav;
	}
	
}
