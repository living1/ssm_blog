package com.java1234.util;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;


public class ResponseUtil {

	//返回一个json流，这样页面就能接收，前端显示后台的东西要么采用${}，或者想直接通过封装json对象来识别，json对象是可以直接识别的
	public static void write(HttpServletResponse response,Object o)throws Exception{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out=response.getWriter();
		out.println(o.toString());
		out.flush();
		out.close();
	}
}
