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
 * 自定义realm
 * @author living
 *
 */
public class MyRealm extends AuthorizingRealm{

	@Resource
	private BloggerService bloggerService;
	/**
	 * 为当前登陆的用户授予角色和权限
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		return null;
	}

	/**
	 * 验证当前登录的用户
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String userName=(String) token.getPrincipal();
		Blogger blogger=bloggerService.getByUserName(userName);
		if(blogger!=null) {//查询到之后有两种情况，一种是密码不正确，一种是密码正确匹配成功
			//获取session，这里是用的shiro提供的
			SecurityUtils.getSubject().getSession().setAttribute("currentUser", blogger);//把当前用户存到session中
			AuthenticationInfo authcInfo= new SimpleAuthenticationInfo(blogger.getUserName(), blogger.getPassword(), "xxx");
			return authcInfo;
		}else {//如果没有查询到就返回空
			return null;
		}
	}

}
