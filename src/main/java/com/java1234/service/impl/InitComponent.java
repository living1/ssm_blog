package com.java1234.service.impl;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.java1234.entity.Blog;
import com.java1234.entity.BlogType;
import com.java1234.entity.Blogger;
import com.java1234.entity.Link;
import com.java1234.service.BlogService;
import com.java1234.service.BlogTypeService;
import com.java1234.service.BloggerService;
import com.java1234.service.LinkService;

//初始化组件
//将不常改动的数据存入application中，系统性能会大大提高
@Component//这个组件也必须要弄成bean，不然会出现空指针异常
public class InitComponent implements ServletContextListener, ApplicationContextAware{

	private static ApplicationContext applicationContext;
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext application=sce.getServletContext();//获取application对象
		BloggerService bloggerService=(BloggerService) applicationContext.getBean("bloggerService");
		Blogger blogger=bloggerService.find();//获取博主信息
		blogger.setPassword(null);//密码较为敏感，将获取到的博主信息中的密码清空
		application.setAttribute("blogger", blogger);
		
		LinkService linkService=(LinkService) applicationContext.getBean("linkService");
		List<Link> linkList=linkService.list(null);//查询所有的友情链接的信息
		application.setAttribute("linkList", linkList);
		
		BlogTypeService blogTypeService=(BlogTypeService) applicationContext.getBean("blogTypeService");
		List<BlogType> blogTypeCountList=blogTypeService.countList(); //查询博客类别以及博客数量
		application.setAttribute("blogTypeCountList", blogTypeCountList);
		
		BlogService blogService=(BlogService) applicationContext.getBean("blogService");
		List<Blog> blogCountList=blogService.countList(); //根据日期分组查询博客
		application.setAttribute("blogCountList", blogCountList);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}

	//获取application，application全局作用域，因为有些信息(博主信息。。)那些的不会经常变动，所以不用每次都刷新加载，可以存到application中
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext=applicationContext;
	}

}
