package com.java1234.dao;

import java.util.List;
import java.util.Map;

import com.java1234.entity.Link;

/**
 * 友情链接Dao接口
 * @author living
 *
 */
public interface LinkDao {

	/**
	 * 查找友情链接信息
	 * @param map
	 * @return
	 */
	public List<Link> list(Map<String, Object> map);
	
	//获取友情链接总数，用于分页
	public Long getTotal(Map<String,Object> map);
	
	//返回的都是操作的记录数
	//添加友情链接信息
	public Integer add(Link link);
	
	//修改友情链接信息
	public Integer update(Link link);
	
	//删除友情链接
	public Integer delete(Integer id);
}
