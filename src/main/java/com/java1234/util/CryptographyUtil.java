package com.java1234.util;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * ���ܹ���
 * @author living
 *
 */
public class CryptographyUtil {

	public static String md5(String str,String salt) {
		return new Md5Hash(str, salt).toString();
	}
	
	public static void main(String[] args) {
		String password="123456";
		
		System.out.println("Md5���ܣ�"+CryptographyUtil.md5(password, "java1234"));
	}
}
