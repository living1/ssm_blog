package com.java1234.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.java1234.dao.BloggerDao;
import com.java1234.entity.Blogger;
import com.java1234.service.BloggerService;

/**
 * 博主service实现类
 * @author living
 *
 */
@Service("bloggerService")
public class BloggerServiceImpl implements BloggerService{

	@Resource
	private BloggerDao bloggerDao;
	
	@Override
	public Blogger getByUserName(String userName) {
		return bloggerDao.getByUserName(userName);
	}

	@Override
	public Blogger find() {
		return bloggerDao.find();
	}

	@Override
	public Integer update(Blogger blogger) {
		return bloggerDao.update(blogger);
	}

}
