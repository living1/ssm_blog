<script type="text/javascript" src="${pageContext.request.contextPath}/static/ueditor/third-party/SyntaxHighlighter/shCore.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/ueditor/third-party/SyntaxHighlighter/shCoreDefault.css">
<script type="text/javascript">
    SyntaxHighlighter.all();
    
    function showOtherComment(){
    	$(".otherComment").show();
    }
    
    function loadimage(){
		document.getElementById("randImage").src="${pageContext.request.contextPath}/image.jsp?"+Math.random();
	}
    
    function submitData(){
    	var content=$("#content").val();//获取内容,这里不是el表达式,是获取dom元素的方法
    	var imageCode=$("#imageCode").val();//获取验证码
    	if(content==null || content==""){
    		alert("请输入评论内容！"); 
    	}else if(imageCode==null || imageCode==""){
    		alert("请填写验证码！");
    	}else{//上面两个都通过了就可以提交了
    		$.post("${pageContext.request.contextPath}/comment/save.do",{"content":content,"imageCode":imageCode,"blog.id":"${blog.id}"},function(result){
    			if(result.success){
    				alert("评论提交成功，审核通过后显示！");
    				//这之后不需要刷新页面，因为必须审核通过后才会显示，所以刷新了也是原样
    				$("#content").val('');
    				$("#imageCode").val('');
    			}else{
    				alert(result.errorInfo);//提交不成功，就回显后台判断出的错误信息。
    			}
    		},"json");//第一个参数是提交的地址,第二个参数是需要传递的参数,这个是以键值对的形式,第三个是回调方法,后面表明这是一个json格式
    	}
    }


</script><!-- 设置高亮显示，uedit的配置 -->

<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="data_list">
	<div class="data_list_title">
		<img src="/static/images/blog_show_icon.png"/>
		博客信息
	</div>
	<div>
		<div class="blog_title"><h3><strong>${blog.title }</strong></h3></div>
		<div class="blog_share">
			<div class="bshare-custom"><a title="分享到QQ空间" class="bshare-qzone"></a><a title="分享到新浪微博" class="bshare-sinaminiblog"></a><a title="分享到人人网" class="bshare-renren"></a><a title="分享到腾讯微博" class="bshare-qqmb"></a><a title="分享到网易微博" class="bshare-neteasemb"></a><a title="更多平台" class="bshare-more bshare-more-icon more-style-addthis"></a><span class="BSHARE_COUNT bshare-share-count">0</span></div><script type="text/javascript" charset="utf-8" src="http://static.bshare.cn/b/buttonLite.js#style=-1&amp;uuid=&amp;pophcol=2&amp;lang=zh"></script><script type="text/javascript" charset="utf-8" src="http://static.bshare.cn/b/bshareC0.js"></script>
		</div>
		<div class="blog_info">
			发布时间：【<fmt:formatDate value="${blog.releaseDate }" type="date" pattern="yyyy-MM-dd HH:mm"/>】&nbsp;&nbsp;博客类别:${blog.blogType.typeName }&nbsp;&nbsp;阅读次数(${blog.clickHit })  评论(${blog.replyHit })
		</div>
		<div class="blog_content">
			${blog.content }
		</div>
		<div class="blog_keyWord">
			<font><strong>关键字:</strong></font>
			<c:choose>
				<c:when test="${keyWords==null }">
					&nbsp;&nbsp;无
				</c:when>
				<c:otherwise>
					<c:forEach var="keyWord" items="${keyWords }">
					<!-- 点击关键字启用查询功能 -->
						&nbsp;&nbsp;&nbsp;&nbsp;<a href="${pageContext.request.contextPath }/blog/q.html?q=${keyWord }" target="_blank">${keyWord }</a>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</div>
		<div class="blog_lastAndNextPage">
			${pageCode }
		</div>
	</div>
</div>

<div class="data_list">
	<div class="data_list_title">
		<img src="/static/images/comment_icon.png"/>
		评论信息
		<c:if test="${commentList.size()>10 }">
			<a href="javascript:showOtherComment()" style="float:right;padding-right:40px;">显示所有评论</a>
		</c:if><!-- 如果评论的数量大于10，那么只会显示10条，但是会在这里出现一个'显示所有' --> 
	</div>
	
	<div class="commentDatas">
		<c:choose>
			<c:when test="${commentList.size()==0 }">
				暂无评论
			</c:when>
			<c:otherwise>
				<c:forEach var="comment" items="${commentList }" varStatus="status"><!-- status可以获取到从0开始的索引 -->
					<c:choose>
						<c:when test="${status.index<10 }">
							<div class="comment">
								<span><font>${status.index+1 }楼&nbsp;&nbsp;&nbsp;&nbsp;${comment.userIp }:</font>${comment.content }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[&nbsp;&nbsp;<fmt:formatDate value="${comment.commentDate }" type="date" pattern="yyyy-MM-dd HH:mm"/>]</span>
							</div>
						</c:when>
						<c:otherwise><!-- 只显示10条评论，多的不显示 -->
							<div class="otherComment"><!-- 样式里面写了不显示 -->
								<span><font>${status.index+1 }楼&nbsp;&nbsp;&nbsp;&nbsp;${comment.userIp }:</font>${comment.content }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[&nbsp;&nbsp;<fmt:formatDate value="${comment.commentDate }" type="date" pattern="yyyy-MM-dd HH:mm"/>]</span>
							</div>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</div>
</div>

<div class="data_list">
	<div class="data_list_title">
		<img src="/static/images/publish_comment_icon.png"/>
		发表评论
	</div>
	<div class="publish_comment">
		<div>
			<textarea style="width:100%;" rows="3" id="content" name="content" placeholder="来说两句..."></textarea>
		</div>
		<div class="verCode">
			验证码:<input type="text" value="" name="imageCode" id="imageCode" size="10" onkeydown="if(event.keyCode==13)form1.submit"/>&nbsp;<img onclick="javascript:loadimage();" title="换一张试试" name="randImage" id="randImage" src="/image.jsp" width="60px" height="20px" border="1" align="absmiddle"/>
		</div>
		<div class="publishButton">
			<button class="btn btn-primary" type="button" onclick="submitData()">发表评论</button>
		</div>
	</div>
</div> 