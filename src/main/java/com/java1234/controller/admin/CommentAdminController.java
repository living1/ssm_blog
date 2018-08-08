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
import com.java1234.entity.Comment;
import com.java1234.entity.PageBean;
import com.java1234.service.CommentService;
import com.java1234.util.ResponseUtil;
import com.java1234.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

//管理员评论控制层
@Controller
@RequestMapping("/admin/comment")
public class CommentAdminController {

	@Resource
	private CommentService commentService;
	
	/**
	 * 分页查询评论信息
	 * @param page easyUI中点击上/下一页就会传到后台page和rows参数，当前页，每页的数据量
	 * @param rows 相当于pageSize
	 * @param state 用于接收查询信息的对象
	 * @param response ajax需要
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public String list(@RequestParam(value="page",required=false)String page,@RequestParam(value="rows",required=false)String rows,@RequestParam(value="state",required=false)String state,HttpServletResponse response)throws Exception {
		PageBean pageBean=new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("state", state);//评论状态
		map.put("start", pageBean.getStart());
		map.put("size", pageBean.getPageSize());
		List<Comment> commentList=commentService.list(map);
		Long total=commentService.getTotal(map);//获取总记录数
		JSONObject result=new JSONObject();
		JsonConfig jsonConfig=new JsonConfig();
		//将某种类型转换成另一种类型,配置将blog中的时间类型转换成字符串类型，最后将整个blogList转换成jsonArray类型
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd"));
		JSONArray jsonArray=JSONArray.fromObject(commentList, jsonConfig);//将blogList转换成jsonArray
		//必须要这样返回easyUI才能解析
		result.put("rows", jsonArray);
		result.put("total", total);
		ResponseUtil.write(response,result);
		return null;
	}
	
	/**
	 * 评论审核
	 * @param ids
	 * @param state
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/review")
	public String review(@RequestParam(value="ids",required=false)String ids,@RequestParam(value="state",required=false)Integer state,HttpServletResponse response)throws Exception{
		String[] idsStr=ids.split(",");
		for(int i=0;i<idsStr.length;i++) {
			Comment comment=new Comment();
			comment.setId(Integer.parseInt(idsStr[i]));
			comment.setState(state);
			commentService.update(comment);
		}
		JSONObject result=new JSONObject();
		result.put("success", true);
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * 评论删除
	 * @param ids
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	public String delete(@RequestParam(value="ids",required=false)String ids,HttpServletResponse response)throws Exception{
		String[] idsStr=ids.split(",");
		for(int i=0;i<idsStr.length;i++) {
			commentService.delete(Integer.parseInt(idsStr[i]));
		}
		JSONObject result=new JSONObject();
		result.put("success", true);
		ResponseUtil.write(response, result);
		return null;
	}
}
