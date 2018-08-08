package com.java1234.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.java1234.entity.Blog;
import com.java1234.entity.PageBean;
import com.java1234.lucene.BlogIndex;
import com.java1234.service.BlogService;
import com.java1234.util.ResponseUtil;
import com.java1234.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

//管理员博客controller层
@Controller
@RequestMapping("/admin/blog")
public class BlogAdminController {

	@Resource
	private BlogService blogService;
	
	private BlogIndex blogIndex=new BlogIndex();
	
	//添加和修改
	@RequestMapping("/save")//一定要添加映射路径
	public String save(Blog blog,HttpServletResponse response)throws Exception {//使用response，到时候将返回信息封装成json流，传到前端页面中
		int resultTotal=0;//返回记录数，如果大于0才能算是操作成功
		if(blog.getId()==null) {//添加
			resultTotal=blogService.add(blog);
			blogIndex.addIndex(blog);
		}else {//修改
			resultTotal=blogService.update(blog);//添加和修改就相差一个id
			//这里同时需要更新索引
			blogIndex.updateIndex(blog);
		}
		JSONObject result=new JSONObject();
		if(resultTotal>0) {
			result.put("success", true);
		}else {
			result.put("success", false);
		}
		ResponseUtil.write(response, result);
		return null;
	}

	
	/**
	 * 分页查询博客信息
	 * @param page easyUI中点击上/下一页就会传到后台page和rows参数，当前页，每页的数据量
	 * @param rows
	 * @param s_blog 用于接收查询信息的对象
	 * @param response ajax需要
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public String list(@RequestParam(value="page",required=false)String page,@RequestParam(value="rows",required=false)String rows,Blog s_blog,HttpServletResponse response)throws Exception {
		PageBean pageBean=new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("title", StringUtil.formatLike(s_blog.getTitle()));//这里直接采用数据库的模糊查询
		map.put("start", pageBean.getStart());
		map.put("size", pageBean.getPageSize());
		List<Blog> blogList=blogService.list(map);
		Long total=blogService.getTotal(map);//获取总记录数
		JSONObject result=new JSONObject();
		JsonConfig jsonConfig=new JsonConfig();
		//将某种类型转换成另一种类型,配置将blog中的时间类型转换成字符串类型，最后将整个blogList转换成jsonArray类型
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd"));
		JSONArray jsonArray=JSONArray.fromObject(blogList, jsonConfig);//将blogList转换成jsonArray
		//必须要这样返回easyUI才能解析
		result.put("rows", jsonArray);
		result.put("total", total);
		ResponseUtil.write(response,result);
		return null;
	}
	
	/**
	 * 博客信息删除
	 * @param ids
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	public String delete(@RequestParam(value="ids",required=false)String ids,HttpServletResponse response)throws Exception{
		String[] idsStr=ids.split(",");
		for(int i=0;i<idsStr.length;i++) {
			blogService.delete(Integer.parseInt(idsStr[i]));
			//删除博客之后同时也要删除索引，不然到时候查找的时候已经删除过得博客照样能查找出来
			blogIndex.deleteIndex(idsStr[i]);
		}
		JSONObject result=new JSONObject();
		result.put("success", true);
		ResponseUtil.write(response, result);
		return null;
	}
	
	//这里的id是一定需要的所以不加required=false,并且只能有一个
	//因为修改的时候需要打开一个新的编辑窗口并且把博客的信息全部回显到编辑窗口中，所以需要通过id查找博客
	@RequestMapping("/findById")
	public String findById(@RequestParam(value="id")String id,HttpServletResponse response)throws Exception{
		Blog blog=blogService.findById(Integer.parseInt(id));
		JSONObject result=JSONObject.fromObject(blog);//将object对象转换为json对象
		ResponseUtil.write(response, result);
		return null;
	}
}
