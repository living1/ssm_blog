package com.java1234.dao;

import java.util.List;
import java.util.Map;

import com.java1234.entity.Link;

/**
 * ��������Dao�ӿ�
 * @author living
 *
 */
public interface LinkDao {

	/**
	 * ��������������Ϣ
	 * @param map
	 * @return
	 */
	public List<Link> list(Map<String, Object> map);
	
	//��ȡ�����������������ڷ�ҳ
	public Long getTotal(Map<String,Object> map);
	
	//���صĶ��ǲ����ļ�¼��
	//�������������Ϣ
	public Integer add(Link link);
	
	//�޸�����������Ϣ
	public Integer update(Link link);
	
	//ɾ����������
	public Integer delete(Integer id);
}
