package com.java1234.util;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;


public class ResponseUtil {

	//����һ��json��������ҳ����ܽ��գ�ǰ����ʾ��̨�Ķ���Ҫô����${}��������ֱ��ͨ����װjson������ʶ��json�����ǿ���ֱ��ʶ���
	public static void write(HttpServletResponse response,Object o)throws Exception{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out=response.getWriter();
		out.println(o.toString());
		out.flush();
		out.close();
	}
}
