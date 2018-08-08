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
 * ����controller
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
	 * ���󲩿���ϸ��Ϣ
	 * @param id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/articles/{id}")
	public ModelAndView details(@PathVariable("id") Integer id,HttpServletRequest request)throws Exception {//��ʱ����ص���URL����������ʹ��@PathVariable
		ModelAndView mav=new ModelAndView();
		Blog blog=blogService.findById(id);
		String keyWords=blog.getKeyWord();//��ȡ�ؼ���
		if(StringUtil.isNotEmpty(keyWords)) {//��Щ����û�йؼ��֣�������ǰ��Ҫ�ж�һ��
			String[] arr=keyWords.split(" ");//�Կո�Ϊ�ָ�������ַ����ֿ�(���ݿ��д洢��ʱ����������ؼ���֮���ÿո����),ע�����aa  bb�м������ո������ֳ�����Ԫ��������������һ���ǿո�
			mav.addObject("keyWords", StringUtil.filterWhite(Arrays.asList(arr)));//filterWhite�Լ���װ�ķ������������˼����еĿո���Ϊ����ķָ�����ܻ���ֿո�
		}else {
			mav.addObject("keyWords", null);
		}
		mav.addObject("blog", blog);//���������ý�ȥ������ǰ���ʾ�û��������ʱ�ĵ���������û��������һ��
		blog.setClickHit(blog.getClickHit()+1);//���µ����
		blogService.update(blog);
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("blogId", blog.getId());//ÿ�����͵�ҳ����ֻ��ʾ������͵�����
		map.put("state", 1);//ֻ��ѯ���ͨ��������
		mav.addObject("commentList", commentService.list(map));//�������
		mav.addObject("pageCode", this.getUpAndDownPageCode(blogService.getLastBlog(id), blogService.getNextBlog(id), request.getServletContext().getContextPath()));//���һ��������ȡ������·��
		mav.addObject("pageTitle", blog.getTitle()+"java��Դ����ϵͳ");//pageTitle���൱��html�����title
		mav.addObject("mainPage", "foreground/blog/view.jsp");//ת����view.jsp
		mav.setViewName("mainTemp");
		return mav;
	}
	
	//��ȡ��һƪ���ͺ���һƪ����
	private String getUpAndDownPageCode(Blog lastBlog,Blog nextBlog,String projectContext) {//projectContext��Ŀ·��
		StringBuffer pageCode=new StringBuffer();
		if(lastBlog==null || lastBlog.getId()==null) {
			pageCode.append("<p>��һƪ:û����</p>");
		}else {
			pageCode.append("<p>��һƪ:<a href='"+projectContext+"/blog/articles/"+lastBlog.getId()+".html'>"+lastBlog.getTitle()+"</a></p>");
		}
		
		if(nextBlog==null || nextBlog.getId()==null) {
			pageCode.append("<p>��һƪ:û����</p>");
		}else {
			pageCode.append("<p>��һƪ:<a href='"+projectContext+"/blog/articles/"+nextBlog.getId()+".html'>"+nextBlog.getTitle()+"</a></p>");
		}
		return pageCode.toString();
	}
	
	//���ݹؼ��ֲ�ѯ��ز�����Ϣ
	@RequestMapping("/q")
	public ModelAndView search(@RequestParam(value="q",required=false) String q,@RequestParam(value="page",required=false) String page,HttpServletRequest request)throws Exception{
		int pageSize=3;
		if(StringUtil.isEmpty(page)) {
			page="1";//���û��page�Ǿ��ǵ�һҳ,��Ϊ��һ��������ʱ��ֻ���������������û��page����������null
		}
		ModelAndView mav=new ModelAndView();
		mav.addObject("pageTitle", "�����ؼ���'"+q+"'���ҳ��_��ţ���");
		mav.addObject("mainPage", "foreground/blog/result.jsp");
		List<Blog> blogList=blogIndex.searchBlog(q);
		Integer toIndex=blogList.size()>=Integer.parseInt(page)*pageSize?Integer.parseInt(page)*pageSize:blogList.size();//ÿһҳ�����ʾ10�����ݣ����������������������һҳ����ʼ����+pageSize����toIndex������һҳ����ʼ����+pageSize,������ǲ��͵�������
		mav.addObject("blogList", blogList.subList((Integer.parseInt(page)-1)*pageSize, toIndex));//��Ϊÿ�ε����һҳ���ᴫ��һ��page���������Ծ��൱��һ��ѭ��
		mav.addObject("pageCode", this.genUpAndDownPageCode(Integer.parseInt(page), blogList.size(), q, pageSize, request.getServletContext().getContextPath()));
		mav.addObject("resultTotal", blogList.size());//ҳ������Ҫ��ʾ��ѯ���ļ�¼��
		mav.addObject("q", q);
		mav.setViewName("mainTemp");
		return mav;
	}
	
	
	/**
	 * ��������ҳ��ҳ�룬���ɵ�ҳ����bootstrap��ܵ�
	 * @param page ���ڷ�ҳ
	 * @param totalNum ���ڷ�ҳ���ܼ�¼��
	 * @param q ������Ϊ����Ĵ��ݲ���
	 * @param pageSize ���ڷ�ҳ
	 * @param projectContext ��ȡ������·��
	 * @return
	 */
	private String genUpAndDownPageCode(Integer page,Integer totalNum,String q,Integer pageSize,String projectContext) {
		//��ȡ��ҳ��
		long totalPage=totalNum%pageSize==0?totalNum/pageSize:totalNum/pageSize+1;
		StringBuffer pageCode=new StringBuffer();
		if(totalPage==0) {
			return "";
		}else {
			pageCode.append("<nav>");
			pageCode.append("<ul class='pager'>");
			if(page>1) {
				pageCode.append("<li><a href='"+projectContext+"/blog/q.html?page="+(page-1)+"&q="+q+"'>��һҳ</a></li>");
			}else {//page�������1�Ǿ�û����һҳ�ˣ�������һҳ���ܵ�
				pageCode.append("<li class='disabled'><a href='#'>��һҳ</a></li>");
			}
			if(page<totalPage) {
				pageCode.append("<li><a href='"+projectContext+"/blog/q.html?page="+(page+1)+"&q="+q+"'>��һҳ</a></li>");
			}else {//����������һҳ�Ǿ�û����һҳ�ˣ��������ó���һҳ���ܵ�
				pageCode.append("<li class='disabled'><a href='#'>��һҳ</a></li>");
			}
			pageCode.append("</ul>");
			pageCode.append("</nav>");
		}
		return pageCode.toString();
	}
}
