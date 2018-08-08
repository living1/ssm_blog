package com.java1234.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.java1234.entity.Blog;
import com.java1234.lucene.BlogIndex;
import com.java1234.service.BlogService;
import com.java1234.service.CommentService;
import com.java1234.util.StringUtil;

/**
 * 博客controller
 * @author living
 *
 */
@Controller
@RequestMapping("/blog")
public class BlogController {

	@Resource
	private BlogService blogService;
	
	@Resource
	private CommentService commentService;
	
	private BlogIndex blogIndex=new BlogIndex();
	
	/**
	 * 请求博客详细信息
	 * @param id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/articles/{id}")
	public ModelAndView details(@PathVariable("id") Integer id,HttpServletRequest request)throws Exception {//到时候加载的是URL参数，所以使用@PathVariable
		ModelAndView mav=new ModelAndView();
		Blog blog=blogService.findById(id);
		String keyWords=blog.getKeyWord();//提取关键字
		if(StringUtil.isNotEmpty(keyWords)) {//有些博客没有关键字，所以提前先要判断一下
			String[] arr=keyWords.split(" ");//以空格为分割符，将字符串分开(数据库中存储的时候就是两个关键字之间用空格隔开),注意如果aa  bb中间两个空格，这样分出来的元素有三个，还有一个是空格
			mav.addObject("keyWords", StringUtil.filterWhite(Arrays.asList(arr)));//filterWhite自己封装的方法，用来过滤集合中的空格，因为上面的分割方法可能会出现空格
		}else {
			mav.addObject("keyWords", null);
		}
		mav.addObject("blog", blog);//将数据设置进去，放在前面表示用户点击进入时的点击量不算用户点击的这一次
		blog.setClickHit(blog.getClickHit()+1);//更新点击量
		blogService.update(blog);
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("blogId", blog.getId());//每个博客的页面上只显示这个博客的评论
		map.put("state", 1);//只查询审核通过的评论
		mav.addObject("commentList", commentService.list(map));//获得评论
		mav.addObject("pageCode", this.getUpAndDownPageCode(blogService.getLastBlog(id), blogService.getNextBlog(id), request.getServletContext().getContextPath()));//最后一个参数获取上下文路径
		mav.addObject("pageTitle", blog.getTitle()+"java开源博客系统");//pageTitle就相当于html里面的title
		mav.addObject("mainPage", "foreground/blog/view.jsp");//转发到view.jsp
		mav.setViewName("mainTemp");
		return mav;
	}
	
	//获取上一篇博客和下一篇博客
	private String getUpAndDownPageCode(Blog lastBlog,Blog nextBlog,String projectContext) {//projectContext项目路径
		StringBuffer pageCode=new StringBuffer();
		if(lastBlog==null || lastBlog.getId()==null) {
			pageCode.append("<p>上一篇:没有了</p>");
		}else {
			pageCode.append("<p>上一篇:<a href='"+projectContext+"/blog/articles/"+lastBlog.getId()+".html'>"+lastBlog.getTitle()+"</a></p>");
		}
		
		if(nextBlog==null || nextBlog.getId()==null) {
			pageCode.append("<p>下一篇:没有了</p>");
		}else {
			pageCode.append("<p>下一篇:<a href='"+projectContext+"/blog/articles/"+nextBlog.getId()+".html'>"+nextBlog.getTitle()+"</a></p>");
		}
		return pageCode.toString();
	}
	
	//根据关键字查询相关博客信息
	@RequestMapping("/q")
	public ModelAndView search(@RequestParam(value="q",required=false) String q,@RequestParam(value="page",required=false) String page,HttpServletRequest request)throws Exception{
		int pageSize=3;
		if(StringUtil.isEmpty(page)) {
			page="1";//如果没有page那就是第一页,因为第一次搜索的时候只是生成搜索结果，没有page穿过来的是null
		}
		ModelAndView mav=new ModelAndView();
		mav.addObject("pageTitle", "搜索关键字'"+q+"'结果页面_海牛大大");
		mav.addObject("mainPage", "foreground/blog/result.jsp");
		List<Blog> blogList=blogIndex.searchBlog(q);
		Integer toIndex=blogList.size()>=Integer.parseInt(page)*pageSize?Integer.parseInt(page)*pageSize:blogList.size();//每一页最多显示10条数据，所以如果博客数量大于这一页的起始索引+pageSize，那toIndex就是这一页的起始索引+pageSize,否则就是博客的数量。
		mav.addObject("blogList", blogList.subList((Integer.parseInt(page)-1)*pageSize, toIndex));//因为每次点击下一页都会传递一个page过来，所以就相当于一个循环
		mav.addObject("pageCode", this.genUpAndDownPageCode(Integer.parseInt(page), blogList.size(), q, pageSize, request.getServletContext().getContextPath()));
		mav.addObject("resultTotal", blogList.size());//页面中需要显示查询到的记录数
		mav.addObject("q", q);
		mav.setViewName("mainTemp");
		return mav;
	}
	
	
	/**
	 * 生成上下页的页码，生成的页码是bootstrap框架的
	 * @param page 用于分页
	 * @param totalNum 用于分页，总记录数
	 * @param q 用来做为请求的传递参数
	 * @param pageSize 用于分页
	 * @param projectContext 获取上下文路径
	 * @return
	 */
	private String genUpAndDownPageCode(Integer page,Integer totalNum,String q,Integer pageSize,String projectContext) {
		//获取总页数
		long totalPage=totalNum%pageSize==0?totalNum/pageSize:totalNum/pageSize+1;
		StringBuffer pageCode=new StringBuffer();
		if(totalPage==0) {
			return "";
		}else {
			pageCode.append("<nav>");
			pageCode.append("<ul class='pager'>");
			if(page>1) {
				pageCode.append("<li><a href='"+projectContext+"/blog/q.html?page="+(page-1)+"&q="+q+"'>上一页</a></li>");
			}else {//page如果等于1那就没有上一页了，所以上一页不能点
				pageCode.append("<li class='disabled'><a href='#'>上一页</a></li>");
			}
			if(page<totalPage) {
				pageCode.append("<li><a href='"+projectContext+"/blog/q.html?page="+(page+1)+"&q="+q+"'>下一页</a></li>");
			}else {//如果就是最后一页那就没有下一页了，所以设置成下一页不能点
				pageCode.append("<li class='disabled'><a href='#'>下一页</a></li>");
			}
			pageCode.append("</ul>");
			pageCode.append("</nav>");
		}
		return pageCode.toString();
	}
}
