<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="data_list">
	<div class="data_list_title">
		<img src="/static/images/search_icon.png"/>
		搜索&nbsp;<font color="red">${q }</font>&nbsp;的结果&nbsp;(总共搜索到&nbsp;${resultTotal }&nbsp;条记录)
	</div>
	<div class="datas search"><!-- 前面的datas是需要的，这下面的title summary link这些样式都是写在datas里面，不加的话显示不出来 -->
		<ul>
			<c:choose>
				<c:when test="${blogList.size()==0 }"><!-- 没有数据的情况下 -->
					<div align="center" style="padding-top:20px">未查询到结果，换个关键字试试看！</div>
				</c:when>
				<c:otherwise>
					<c:forEach var="blog" items="${blogList }">
						<li style="margin-bottom: 20px">
						  	<span class="title"><a href="${pageContext.request.contextPath}/blog/articles/${blog.id }.html" target="_blank">${blog.title }</a></span>
						  	<span class="summary">摘要:${blog.content }...</span>
						  	<span class="link"><a href="${pageContext.request.contextPath}/blog/articles/${blog.id }.html">http://blog.hainiudada.com/blog/articles/${blog.id }.html</a>&nbsp;&nbsp;&nbsp;&nbsp;发布日期：${blog.releaseDateStr }</span>
						  </li>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</ul>
	</div>
	${pageCode }
</div>