package com.java1234.dao;

import com.java1234.entity.Blogger;

/**
 * ����dao�ӿ�
 * @author living
 *
 */
public interface BloggerDao {

	/**
	 * ͨ���û�����ѯ�û�
	 * @param userName
	 * @return
	 */
	public Blogger getByUserName(String userName);
	
	/**
	 * ��ѯ������Ϣ
	 * @return
	 */
	public Blogger find();
	
	//���²�����Ϣ
	public Integer update(Blogger blogger);
}
