package com.java1234.service;

import java.util.List;
import java.util.Map;

import com.java1234.entity.Comment;

//����service�ӿ�
public interface CommentService {

	//��ѯ�û�������Ϣ
	public List<Comment> list(Map<String, Object> map);
	
	//�������
	public int add(Comment comment);
	
	//��ȡ�ܼ�¼��
	public Long getTotal(Map<String,Object> map);
	
	//��������(�������),����һ��ʵ������������չ����
	public int update(Comment comment);
	
	//ɾ��
	public Integer delete(Integer id);
}
