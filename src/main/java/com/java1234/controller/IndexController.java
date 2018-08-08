package com.java1234.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.java1234.entity.Blog;
import com.java1234.entity.PageBean;
import com.java1234.service.BlogService;
import com.java1234.util.PageUtil;
import com.java1234.util.StringUtil;

/**
 * 主页Controller
 * @author living
 *
 */
@Controller
@RequestMapping("/")//请求的时候index.html就是根目录，所以什么都不加
public class IndexController {

	@Resource
	private BlogService blogService;
	
	/**
	 * 请求主页
	 * @return
	 */
	//springMVC后台控制层获取参数的方式主要有两种，一种是request.getParameter("name"),
	//另一种是使用注解@RequestParam直接获取
	//这里表示对传入的参数指定为page,如果前端不传page参数名，那么回个参数赋值为null
	@RequestMapping("/index")
	public ModelAndView index(@RequestParam(value="page",required=false)String page, @RequestParam(value="typeId",required=false)String typeId, @RequestParam(value="releaseDateStr",required=false)String releaseDateStr, HttpServletRequest request) throws Exception{
		ModelAndView mav = new ModelAndView();
		if(StringUtil.isEmpty(page)) {
			page="1";
		}
		PageBean pageBean = new PageBean(Integer.parseInt(page), 10);//每页十个，page是起始页
		Map<String, Object> map= new HashMap<String, Object>();
		map.put("start", pageBean.getStart());
		map.put("size", pageBean.getPageSize());
		map.put("typeId", typeId);
		map.put("releaseDateStr", releaseDateStr);
		List<Blog> blogList=blogService.list(map);
		for(Blog blog:blogList) {
			List<String> imageList = blog.getImageList();
			String blogInfo = blog.getContent();//博客的内容就是HTML
			Document doc=Jsoup.parse(blogInfo);
			Elements jpgs=doc.select("img[src$=.jpg]");//选出所有后缀名是jpg的元素
			for(int i=0; i<jpgs.size();i++) {
				Element jpg= jpgs.get(i);
				imageList.add(jpg.toString());
				if(i==2) {
					break;//控制图片数量<=3
				}
			}
		}
		mav.addObject("blogList", blogList);
		StringBuffer param = new StringBuffer();//param是一个条件参数，比如点击了侧栏上某个类别或时间的博客，就会把这些信息存入param，以生成专门页面的代码
		if(StringUtil.isNotEmpty(typeId)) {
			param.append("typeId="+typeId+"&");//如果后面还有其他的参数就要加上&拼接，其实生成的页面在param的前面已经加上了&。
		}
		if(StringUtil.isNotEmpty(releaseDateStr)) {
			param.append("releaseDateStr="+releaseDateStr+"&");//如果后面还有其他的参数就要加上&拼接，其实生成的页面在param的前面已经加上了&。
		}
		mav.addObject("pageCode", PageUtil.genPagination(request.getContextPath()+"/index.html", blogService.getTotal(map), Integer.parseInt(page), 10, param.toString()));
		mav.addObject("pageTitle", "罗佐的博客系统");
		mav.addObject("mainPage", "foreground/blog/list.jsp");
		mav.setViewName("mainTemp");//跳转到mainTemp这个页面
		return mav;
	}
	
	//源码下载
	@RequestMapping("/download")
	public ModelAndView download()throws Exception{
		ModelAndView mav=new ModelAndView();
		mav.addObject("pageTitle", "本站源码下载_java开源博客系统");
		mav.addObject("mainPage", "foreground/system/download.jsp");
		mav.setViewName("mainTemp");
		return mav;
	}
}
