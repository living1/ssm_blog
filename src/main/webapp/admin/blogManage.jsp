<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>博客管理页面</title>
<!-- 后台使用的是easyUI，所以必须要引入这些 -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/icon.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
//val表示当前的值就是title的值，row代表整个对象，可以取到所有列的值
	function formatTitle(val,row){
		return "<a target='_blank' href='${pageContext.request.contextPath}/blog/articles/"+row.id+".html'>"+val+"</a>";
	}
	
	function formatBlogType(val,row){
		return val.typeName;//val代表的是这个field中的东西，这里代表的就是一个blogType对象，通过级联的方式来获得想要信息(类别名)
	}
	
	function searchBlog(){
		$("#dg").datagrid('load',{
			"title":$("#s_title").val()
		});
	}
	
	function deleteBlog(){
		var selectedRows=$("#dg").datagrid("getSelections");//能够获取所有选中的行对象，返回一个类似数组的东西
		if(selectedRows.length==0){//如果没有选
			$.messager.alert("系统提示","请选择要删除的数据!");
			return;
		}
		var strIds=[];//选了的话就新建一个数组来存放选择的行的id
		for(var i=0;i<selectedRows.length;i++){
			strIds.push(selectedRows[i].id);
		}
		var ids=strIds.join(",");//将数组中的每个元素用","隔开，形成一个字符串
		//第二个function是回调函数
		$.messager.confirm("系统提示","您确定要删除这<font color='red'>"+selectedRows.length+"</font>条数据吗?",function(r){
			if(r){//r表示用户点击的按钮，确认(true),取消(false)
				//post提交这里会请求后台，这里才会得到后台的响应，所以result出现的回调位置应该是这里
				$.post("${pageContext.request.contextPath}/admin/blog/delete.do",{ids:ids},function(result){
					if(result.success){
						$.messager.alert("系统提示","数据已成功删除！");
						$("#dg").datagrid("reload");//刷新数据
					}else{
						$.messager.alert("系统提示","数据删除失败！");
					}
				},"json");//表明这是一个json格式,也只会出现在post请求后台这里
			}
		});
	}
	
	function openBlogModifyTab(){
		var selectedRows=$("#dg").datagrid("getSelections");
		if(selectedRows.length!=1){
			$.messager.alert("系统提示","请选择一条数据!");
			return;
		}
		var row=selectedRows[0];//行对象数组
		//main当中嵌套的iframe，然后来显示不同页面的内容，通过这个window.parent能调用父窗口中的方法
		window.parent.openTab('修改博客','modifyBlog.jsp?id='+row.id,'icon-writeblog');//main.jsp中的方法
	}
</script>
</head>
<body style="margin:1px;">
<!-- fitColumns自适应列 -->
<!-- pagination开启分页组件  rowNumbers显示行号(不是编号) -->
<!-- toolbar工具条组件(增删改查那些按钮和搜索框都是写在那里面的) -->
<table class="easyui-datagrid" id="dg" title="博客管理" fitColumns="true" pagination="true"
 rownumbers="true" url="${pageContext.request.contextPath }/admin/blog/list.do" fit="true"
 toolbar="#tb">
 	<thead>
 		<tr>
 		<!-- field对应后台,这个属性的名字一定要和后台的json相对应，因为到时候会自动匹配 -->
 			<th field="cb" checkbox="true" align="center"></th>
 			<th field="id" width="20px" align="center">编号</th>
 			<!-- 这里需要弄成超链接的形式，所以需要加上formatter -->
 			<th field="title" width="200px" align="center" formatter="formatTitle">标题</th>
 			<th field="releaseDate" width="50px" align="center">发布日期</th>
 			<th field="blogType" width="50px" align="center" formatter="formatBlogType">博客类型</th>
 		</tr>
 	</thead>
 </table>
 <!-- 工具条 -->
 <div id="tb">
	<div>
		<a href="javascript:openBlogModifyTab()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a>
		<a href="javascript:deleteBlog()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
	</div>
 	<div><!-- 通过标题来查询 -->
 	<!-- onkeydown按下enter就是搜索(这里直接使用的是input的一个事件模型，没有添加搜索按钮都能直接查询),event.keyCode==13这个代表的就是按下enter事件,如果触发了这个事件就会执行searchBlog方法 -->
 		&nbsp;标题&nbsp;<input type="text" id="s_title" size="20px" onkeydown="if(event.keyCode==13) searchBlog()"/>
 		<a href="javascript:searchBlog()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a><!-- 搜索按钮,plain去掉边框 -->
 	</div>
 </div>
</body>
</html>