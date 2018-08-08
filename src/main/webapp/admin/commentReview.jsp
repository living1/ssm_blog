<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>评论审核页面</title>
<!-- 后台使用的是easyUI，所以必须要引入这些 -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/icon.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">

 	function formatBlogTitle(val,row){//当前值(field中是blog，所以这里val就是一个blog对象，comment对应的blog对象)，行对象(参数)
 		if(val==null){
 			return "<font color='red'>该博客已删除!</font>"
 		}else{
 			return "<a target='_blank' href='${pageContext.request.contextPath}/blog/articles/"+val.id+".html'>"+val.title+"</a>"
 		}
 	}
 	
 	function commentReview(state){
 		var selectedRows=$("#dg").datagrid("getSelections");//能够获取所有选中的行对象，返回一个类似数组的东西
		if(selectedRows.length==0){//如果没有选
			$.messager.alert("系统提示","请选择要审核的评论!");
			return;
		}
		var strIds=[];//选了的话就新建一个数组来存放选择的行的id
		for(var i=0;i<selectedRows.length;i++){
			strIds.push(selectedRows[i].id);
		}
		var ids=strIds.join(",");//将数组中的每个元素用","隔开，形成一个字符串
		//第二个function是回调函数
		$.messager.confirm("系统提示","您确定要审核这<font color='red'>"+selectedRows.length+"</font>条数据吗?",function(r){
			if(r){//r表示用户点击的按钮，确认(true),取消(false)
				//post提交这里会请求后台，这里才会得到后台的响应，所以result出现的回调位置应该是这里
				$.post("${pageContext.request.contextPath}/admin/comment/review.do",{ids:ids,state:state},function(result){
					if(result.success){
						$.messager.alert("系统提示","提交成功！");
						//$("#dg").datagrid("reload");//刷新数据，也可以用这个
						window.location.reload();//重新加载本页面
					}else{
						$.messager.alert("系统提示","提交失败！");
					}
				},"json");//表明这是一个json格式,也只会出现在post请求后台这里
			}
		});
 	}
</script>
</head>
<body style="margin:1px;">
<!-- fitColumns自适应列 -->
<!-- pagination开启分页组件  rowNumbers显示行号(不是编号) -->
<!-- toolbar工具条组件(增删改查那些按钮和搜索框都是写在那里面的) -->
<!-- 查询出待审核的评论state=0 -->
<table class="easyui-datagrid" id="dg" title="评论审核管理" fitColumns="true" pagination="true"
 rownumbers="true" url="${pageContext.request.contextPath }/admin/comment/list.do?state=0" fit="true" toolbar="#tb"><!-- 这里是引用id是tb的模块作为工具栏 -->
 	<thead>
 		<tr>
 		<!-- field对应后台,这个属性的名字一定要和后台的json相对应，因为到时候会自动匹配 -->
 			<th field="cb" checkbox="true" align="center"></th>
 			<th field="id" width="20px" align="center">编号</th>
 			<!-- 这里需要弄成超链接的形式，所以需要加上formatter -->
 			<th field="blog" width="200px" align="center" formatter="formatBlogTitle">博客标题</th>
 			<th field="userIp" width="100px" align="center">用户IP</th>
 			<th field="content" width="200px" align="center">评论内容</th>
 			<th field="commentDate" width="50px" align="center">评论日期</th>
 		</tr>
 	</thead>
 </table>
 
  <!-- 工具条 -->
 <div id="tb">
	<div>
	<!-- commentReview(1)传递参数，1代表审核通过，2代表不通过 -->
		<a href="javascript:commentReview(1)" class="easyui-linkbutton" iconCls="icon-ok" plain="true">审核通过</a>
		<a href="javascript:commentReview(2)" class="easyui-linkbutton" iconCls="icon-no" plain="true">审核不通过</a>
	</div>
 </div>
</body>
</html>