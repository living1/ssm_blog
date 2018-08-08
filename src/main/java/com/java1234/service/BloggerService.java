package com.java1234.service;

import com.java1234.entity.Blogger;

/**
 * ����service�ӿ�
 * @author living
 *
 */
public interface BloggerService {

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
