package com.java1234.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.java1234.dao.LinkDao;
import com.java1234.entity.Link;
import com.java1234.service.LinkService;

/**
 * 友情链接service实现类
 * @author living
 *
 */
@Service("linkService")
public class LinkServiceImpl implements LinkService{

	@Resource
	private LinkDao linkDao;

	@Override
	public List<Link> list(Map<String, Object> map) {
		return linkDao.list(map);
	}

	@Override
	public Long getTotal(Map<String, Object> map) {
		return linkDao.getTotal(map);
	}

	@Override
	public Integer add(Link link) {
		return linkDao.add(link);
	}

	@Override
	public Integer update(Link link) {
		return linkDao.update(link);
	}

	@Override
	public Integer delete(Integer id) {
		return linkDao.delete(id);
	}
	
	

}
