package com.java1234.dao;

import java.util.List;
import java.util.Map;

import com.java1234.entity.BlogType;

/**
 * 博客类型Dao接口
 * @author living
 *
 */
public interface BlogTypeDao {

	/**
	 * 查询所有博客类型，以及对应的博客数量
	 * @return
	 */
	public List<BlogType> countList();
	
	/**
	 * 通过id查找博客类别实体
	 * @param id
	 * @return
	 */
	public BlogType findById(Integer id);
	
	//分页查询博客类别信息，map用于接收分页信息
	public List<BlogType> list(Map<String,Object> map);
	
	//获取博客类别总数，用于分页
	public Long getTotal(Map<String,Object> map);
	
	//返回的都是操作的记录数
	//添加博客类别信息
	public Integer add(BlogType blogType);
	
	//修改博客类别信息
	public Integer update(BlogType blogType);
	
	//删除博客类别
	public Integer delete(Integer id);
	
}
