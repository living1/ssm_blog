package com.java1234.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.java1234.dao.CommentDao;
import com.java1234.entity.Comment;
import com.java1234.service.CommentService;

//要加注解，不然spring找不到，无法实例化
@Service("commentService")
public class CommentServiceImpl implements CommentService{

	@Resource
	private CommentDao commentDao;
	
	@Override
	public List<Comment> list(Map<String, Object> map) {
		return commentDao.list(map);
	}

	@Override
	public int add(Comment comment) {
		return commentDao.add(comment);
	}

	@Override
	public Long getTotal(Map<String, Object> map) {
		return commentDao.getTotal(map);
	}

	@Override
	public int update(Comment comment) {
		return commentDao.update(comment);
	}

	@Override
	public Integer delete(Integer id) {
		return commentDao.delete(id);
	}

}
