package com.java1234.realm;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.java1234.entity.Blogger;
import com.java1234.service.BloggerService;

/**
 * �Զ���realm
 * @author living
 *
 */
public class MyRealm extends AuthorizingRealm{

	@Resource
	private BloggerService bloggerService;
	/**
	 * Ϊ��ǰ��½���û������ɫ��Ȩ��
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		return null;
	}

	/**
	 * ��֤��ǰ��¼���û�
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String userName=(String) token.getPrincipal();
		Blogger blogger=bloggerService.getByUserName(userName);
		if(blogger!=null) {//��ѯ��֮�������������һ�������벻��ȷ��һ����������ȷƥ��ɹ�
			//��ȡsession���������õ�shiro�ṩ��
			SecurityUtils.getSubject().getSession().setAttribute("currentUser", blogger);//�ѵ�ǰ�û��浽session��
			AuthenticationInfo authcInfo= new SimpleAuthenticationInfo(blogger.getUserName(), blogger.getPassword(), "xxx");
			return authcInfo;
		}else {//���û�в�ѯ���ͷ��ؿ�
			return null;
		}
	}

}