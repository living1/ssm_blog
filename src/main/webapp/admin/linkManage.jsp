<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>友情链接管理页面</title>
<!-- 后台使用的是easyUI，所以必须要引入这些 -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/icon.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">

 	var url;
 	
	function openLinkAddDialog(){
		$("#dlg").dialog("open").dialog("setTitle","添加友情链接信息");
		url="${pageContext.request.contextPath}/admin/link/save.do";
	}
	
	function openLinkModifyDialog(){
		var selectedRows=$("#dg").datagrid("getSelections");
		if(selectedRows.length!=1){
			$.messager.alert("系统提示","请选择一个要修改的友情链接！");
			return;
		}
		var row=selectedRows[0];
		
		$("#dlg").dialog("open").dialog("setTitle","修改友情链接信息");
		$("#fm").form("load",row);
		url="${pageContext.request.contextPath}/admin/link/save.do?id="+row.id;
	}
	
	function saveLink(){
		$("#fm").form("submit",{
			url:url,
			onSubmit:function(){
				return $(this).form("validate");
			},
			success:function(result){
				var result=eval('('+result+')');
				if(result.success){
					$.messager.alert("系统提示","保存成功！");
					resetValue();
					$("#dlg").dialog("close");
					$("#dg").datagrid("reload");
				}else{
					$.messager.alert("系统提示","保存失败！");
					return;
				}
			}
		});
	}
	
	//重置表单
	function resetValue(){
		$("#linkName").val("");
		$("#linkUrl").val("");
		$("#orderNo").val("");
	}
	
	function deleteLink(){
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
				$.post("${pageContext.request.contextPath}/admin/link/delete.do",{ids:ids},function(result){
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
</script>
</head>
<body style="margin:1px;">
<!-- fitColumns自适应列 -->
<!-- pagination开启分页组件  rowNumbers显示行号(不是编号) -->
<!-- toolbar工具条组件(增删改查那些按钮和搜索框都是写在那里面的) -->
<table class="easyui-datagrid" id="dg" title="友情链接管理" fitColumns="true" pagination="true"
 rownumbers="true" url="${pageContext.request.contextPath }/admin/link/list.do" fit="true"
 toolbar="#tb">
 	<thead>
 		<tr>
 		<!-- field对应后台,这个属性的名字一定要和后台的json相对应，因为到时候会自动匹配 -->
 			<th field="cb" checkbox="true" align="center"></th>
 			<th field="id" width="20px" align="center">编号</th>
 			<!-- 这里需要弄成超链接的形式，所以需要加上formatter -->
 			<th field="linkName" width="200px" align="center">友情链接名称</th>
 			<th field="linkUrl" width="200px" align="center">友情链接地址</th>
 			<th field="orderNo" width="100px" align="center">排序序号</th>
 		</tr>
 	</thead>
 </table>
 <!-- 工具条 -->
 <div id="tb">
	<div>
		<a href="javascript:openLinkAddDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a>
		<a href="javascript:openLinkModifyDialog()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a>
		<a href="javascript:deleteLink()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
	</div>
 </div>
 
 <!-- closed可以关闭的，这样就会出现一个关闭的叉 -->
 <!-- buttons窗口中出现的确定/取消按钮 --> 
 <div id="dlg" class="easyui-dialog" style="width:500px;height:200px;padding:10px 20px;" closed="true" buttons="#dlg-buttons">
 	<form method="post" id="fm"><!-- 注意这里action是提交地址，method是提交的方式，如果这里不写method，那到时候设置的url的带参数方式是不起作用的，因为get和post的带参数方式不同 -->
 		<table cellspacing="8px">
 			<tr>
 				<td>友情链接名称:</td>
 				<td>
 				<!-- 设置这是一个验证框 -->
 					<input type="text" id="linkName" name="linkName" class="easyui-validatebox" required="true"/>
 				</td>
 			</tr>
 			<tr>
 				<td>友情链接地址:</td>
 				<td>
 				<!-- 设置这是一个验证框validType="url",验证这里面必须输入URL -->
 					<input type="text" id="linkUrl" name="linkUrl" class="easyui-validatebox" validType="url" required="true" style="width: 250px;"/>
 				</td>
 			</tr>
 			<tr>
 				<td>友情链接排序:</td>
 				<td>
 				<!-- easyui-numberbox设置这个框只能填数字 -->
 					<input type="text" id="orderNo" name="orderNo" class="easyui-numberbox" required="true" style="width:60px;"/>&nbsp;(友情链接序号从小到大排序)
 				</td>
 			</tr>
 		</table>
 	</form>
 </div>
<div id="dlg-buttons">
	<a href="javascript:saveLink()" class="easyui-linkbutton" iconCls="icon-ok" plain="true">保存</a>
	<a href="javascript:closeLinkDialog()" class="easyui-linkbutton" iconCls="icon-cancel" plain="true">关闭</a>
</div>
</body>
</html>