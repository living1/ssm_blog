package com.java1234.controller.admin;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.java1234.entity.Blog;
import com.java1234.entity.BlogType;
import com.java1234.entity.Blogger;
import com.java1234.entity.Link;
import com.java1234.service.BlogService;
import com.java1234.service.BlogTypeService;
import com.java1234.service.BloggerService;
import com.java1234.service.LinkService;
import com.java1234.util.ResponseUtil;

import net.sf.json.JSONObject;

/**
 * 管理员系统controller层
 * @author living
 *
 */
@Controller
@RequestMapping("/admin/system")
public class SystemAdminController {

	@Resource
	private BloggerService bloggerService;
	@Resource
	private LinkService linkService;
	@Resource
	private BlogTypeService blogTypeService;
	@Resource
	private BlogService blogService;
	
	/**
	 * 刷新系统缓存 从数据库重新取一次，然后存到application中
	 * @param request 获取application
	 *  @param response ajax请求
	 * @return
	 */
	@RequestMapping("/refreshSystem")
	public String refreshSystem(HttpServletRequest request,HttpServletResponse response)throws Exception{
		//ServletContext(application作用域的对象)
		ServletContext application=RequestContextUtils.getWebApplicationContext(request).getServletContext();
		
		Blogger blogger=bloggerService.find();//获取博主信息
		blogger.setPassword(null);//密码较为敏感，将获取到的博主信息中的密码清空
		application.setAttribute("blogger", blogger);
		
		List<Link> linkList=linkService.list(null);//查询所有的友情链接的信息
		application.setAttribute("linkList", linkList);
		
		List<BlogType> blogTypeCountList=blogTypeService.countList(); //查询博客类别以及博客数量
		application.setAttribute("blogTypeCountList", blogTypeCountList);
		
		List<Blog> blogCountList=blogService.countList(); //根据日期分组查询博客
		application.setAttribute("blogCountList", blogCountList);
		
		JSONObject result=new JSONObject();
		result.put("success", true);
		ResponseUtil.write(response, result);
		return null;
	}
}
