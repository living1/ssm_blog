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

//���ۿ��Ʋ�
@Controller
@RequestMapping("/comment")
public class CommentController {

	@Resource
	private CommentService commentService;

	@Resource // ÿ������ֻ�ж�Ҫ�޸Ĳ��͵Ļظ������������ҲҪע��
	private BlogService blogService;

	//��ӻ����޸�����
	@RequestMapping("/save")
	public String save(Comment comment, @RequestParam("imageCode") String imageCode, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {// response��ajax���ص�ʱ����Ҫ��
		//�ж���֤���Ƿ���ȷ���������ȷֱ�ӷ���
		String sRand=(String) session.getAttribute("sRand");//�Ȼ�ȡ��ȷ����֤��
		JSONObject result=new JSONObject();
		int resultTotal=0;//���ؼ�¼����ִ����Ӳ������ص���ֵ������0������ӻ��޸ĳɹ�
		if(!imageCode.equals(sRand)) {
			result.put("success", false);
			result.put("errorInfo", "��֤����д����");
		}else {
			String userIp=request.getRemoteAddr();//��ȡ�û�ip���Լ��ڱ��ز��Ե�ʱ���¼����tomcat�Ĵ���ip0:0:0:0:0:0:0:1
			comment.setUserIp(userIp);
			if(comment.getId()==null) {//����ӵ�����
				resultTotal=commentService.add(comment);
				//����blog�����͵Ļظ�������1
				Blog blog=blogService.findById(comment.getBlog().getId());
				blog.setReplyHit(blog.getReplyHit()+1);
				blogService.update(blog);
			}else {//�޸�
				
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
