package com.java1234.service;

import com.java1234.entity.Blogger;

/**
 * 博主service接口
 * @author living
 *
 */
public interface BloggerService {

	/**
	 * 通过用户名查询用户
	 * @param userName
	 * @return
	 */
	public Blogger getByUserName(String userName);
	
	/**
	 * 查询博主信息
	 * @return
	 */
	public Blogger find();
	
	//更新博主信息
	public Integer update(Blogger blogger);
}
