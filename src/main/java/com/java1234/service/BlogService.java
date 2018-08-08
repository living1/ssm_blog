package com.java1234.service;

import java.util.List;
import java.util.Map;

import com.java1234.entity.Blog;

public interface BlogService {

	/**
	 * 根据日期月份分组查询
	 * @return
	 */
	public List<Blog> countList();
	
	/**
	 * 分页查询博客
	 * @param map
	 * @return
	 */
	public List<Blog> list(Map<String, Object> map);
	
	/**
	 * 获取总记录数
	 * @param map
	 * @return
	 */
	public Long getTotal(Map<String, Object> map);
	
	/**
	 * 根据id查找实体
	 * @param id
	 * @return
	 */
	public Blog findById(Integer id);
	
	/**
	 * 更新博客
	 * @param blog
	 * @return
	 */
	public Integer update(Blog blog);
	
	/**
	 * 获取上一篇博客
	 * @param id
	 * @return
	 */
	public Blog getLastBlog(Integer id);
	
	/**
	 * 获取下一篇博客
	 * @param id
	 * @return
	 */
	public Blog getNextBlog(Integer id);

	//添加博客信息
	public Integer add(Blog blog);
	
	//删除博客信息
	public Integer delete(Integer id);
	
	//查询指定博客类别下的博客数量
	public Integer getBlogByTypeId(Integer typeId);
}
