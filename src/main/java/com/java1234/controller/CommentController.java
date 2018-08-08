package com.java1234.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.java1234.entity.Blog;
import com.java1234.entity.Comment;
import com.java1234.service.BlogService;
import com.java1234.service.CommentService;
import com.java1234.util.ResponseUtil;

import net.sf.json.JSONObject;

//评论控制层
@Controller
@RequestMapping("/comment")
public class CommentController {

	@Resource
	private CommentService commentService;

	@Resource // 每次评论只有都要修改博客的回复量，所以这个也要注入
	private BlogService blogService;

	//添加或者修改评论
	@RequestMapping("/save")
	public String save(Comment comment, @RequestParam("imageCode") String imageCode, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {// response是ajax返回的时候需要的
		//判断验证码是否正确，如果不正确直接返回
		String sRand=(String) session.getAttribute("sRand");//先获取正确的验证码
		JSONObject result=new JSONObject();
		int resultTotal=0;//返回记录数，执行添加操作返回的数值，大于0才算添加或修改成功
		if(!imageCode.equals(sRand)) {
			result.put("success", false);
			result.put("errorInfo", "验证码填写错误！");
		}else {
			String userIp=request.getRemoteAddr();//获取用户ip，自己在本地测试的时候记录的是tomcat的代理ip0:0:0:0:0:0:0:1
			comment.setUserIp(userIp);
			if(comment.getId()==null) {//新添加的评论
				resultTotal=commentService.add(comment);
				//更新blog，博客的回复次数加1
				Blog blog=blogService.findById(comment.getBlog().getId());
				blog.setReplyHit(blog.getReplyHit()+1);
				blogService.update(blog);
			}else {//修改
				
			}
		}
		if(resultTotal>0) {
			result.put("success", true);
		}else {
			result.put("success", false);
		}
		ResponseUtil.write(response, result);
		return null;
	}
}
