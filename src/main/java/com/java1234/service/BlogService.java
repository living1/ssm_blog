package com.java1234.service;

import java.util.List;
import java.util.Map;

import com.java1234.entity.Blog;

public interface BlogService {

	/**
	 * ���������·ݷ����ѯ
	 * @return
	 */
	public List<Blog> countList();
	
	/**
	 * ��ҳ��ѯ����
	 * @param map
	 * @return
	 */
	public List<Blog> list(Map<String, Object> map);
	
	/**
	 * ��ȡ�ܼ�¼��
	 * @param map
	 * @return
	 */
	public Long getTotal(Map<String, Object> map);
	
	/**
	 * ����id����ʵ��
	 * @param id
	 * @return
	 */
	public Blog findById(Integer id);
	
	/**
	 * ���²���
	 * @param blog
	 * @return
	 */
	public Integer update(Blog blog);
	
	/**
	 * ��ȡ��һƪ����
	 * @param id
	 * @return
	 */
	public Blog getLastBlog(Integer id);
	
	/**
	 * ��ȡ��һƪ����
	 * @param id
	 * @return
	 */
	public Blog getNextBlog(Integer id);

	//��Ӳ�����Ϣ
	public Integer add(Blog blog);
	
	//ɾ��������Ϣ
	public Integer delete(Integer id);
	
	//��ѯָ����������µĲ�������
	public Integer getBlogByTypeId(Integer typeId);
}
