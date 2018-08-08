package com.java1234.dao;

import java.util.List;
import java.util.Map;

import com.java1234.entity.Comment;

//评论dao接口
public interface CommentDao {

	//查询用户评论信息
	public List<Comment> list(Map<String, Object> map);
	
	//添加评论
	public int add(Comment comment);
	
	//获取总记录数
	public Long getTotal(Map<String,Object> map);
	
	//更新评论(用于审核),传入一个实体这样便于扩展功能
	public int update(Comment comment);
	
	//删除
	public Integer delete(Integer id);
}
