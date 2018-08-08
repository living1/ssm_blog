package com.java1234.dao;

import java.util.List;
import java.util.Map;

import com.java1234.entity.BlogType;

/**
 * ��������Dao�ӿ�
 * @author living
 *
 */
public interface BlogTypeDao {

	/**
	 * ��ѯ���в������ͣ��Լ���Ӧ�Ĳ�������
	 * @return
	 */
	public List<BlogType> countList();
	
	/**
	 * ͨ��id���Ҳ������ʵ��
	 * @param id
	 * @return
	 */
	public BlogType findById(Integer id);
	
	//��ҳ��ѯ���������Ϣ��map���ڽ��շ�ҳ��Ϣ
	public List<BlogType> list(Map<String,Object> map);
	
	//��ȡ����������������ڷ�ҳ
	public Long getTotal(Map<String,Object> map);
	
	//���صĶ��ǲ����ļ�¼��
	//��Ӳ��������Ϣ
	public Integer add(BlogType blogType);
	
	//�޸Ĳ��������Ϣ
	public Integer update(BlogType blogType);
	
	//ɾ���������
	public Integer delete(Integer id);
	
}
