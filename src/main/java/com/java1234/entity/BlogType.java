package com.java1234.entity;


/**
 * ��������ʵ��
 * @author living
 *
 */
public class BlogType {

	private Integer id; //���
	private String typeName; //������������
	private Integer orderNo; //������� ��С��������
	private Integer blogCount; //����,����ֶ���mapper���治��д����Ϊû�ж�Ӧ�����ݿ���ֶ�
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	public Integer getBlogCount() {
		return blogCount;
	}
	public void setBlogCount(Integer blogCount) {
		this.blogCount = blogCount;
	}
	
	
}