<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><!-- c标签，引入这个才能使用c:forEach -->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>修改博客页面</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/icon.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>

<script type="text/javascript" charset="gbk" src="${pageContext.request.contextPath}/static/ueditor/ueditor.config.js"></script>
<script type="text/javascript" charset="gbk" src="${pageContext.request.contextPath}/static/ueditor/ueditor.all.min.js"> </script>
<!--建议手动加在语言，避免在ie下有时因为加载语言失败导致编辑器加载失败-->
<!--这里加载的语言文件会覆盖你在配置项目里添加的语言类型，比如你在配置项目里配置的是英文，这里加载的中文，那最后就是中文-->
<script type="text/javascript" charset="gbk" src="${pageContext.request.contextPath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>

<script type="text/javascript">
	function submitData(){
		//先取值
		var title=$("#title").val();
		var blogTypeId=$("#blogTypeId").combobox("getValue");//获取下拉框中的内容
		var content=UE.getEditor('editor').getContent();
		var keyWord=$("#keyWord").val();
		//判断用户输入的值是否合理
		if(title==null || title==''){
			alert("请输入标题!");
		}else if(blogTypeId==null || blogTypeId==''){
			alert("请选择博客类别!");
		}else if(content==null || content==''){
			alert("博客内容不能为空!");
		}else{//所有的验证通过就开始发起请求，summary获取的是纯文本,截取其中的前155个字符
			//这里需要比写博客多带一个id过去，因为这里是修改，本身就是有id的，如果不添加id就会被后台识别成添加博客
			$.post("${pageContext.request.contextPath}/admin/blog/save.do",{'id':'${param.id}','title':title,'blogType.id':blogTypeId,
				'contentNoTag':UE.getEditor('editor').getContentTxt(),
				'content':content,'summary':UE.getEditor('editor').getContentTxt().substr(0,155),'keyWord':keyWord},function(result){
					if(result.success){
						alert("博客修改成功！");
						//这下面最好不要重置，因为有的时候发现修改的不对，可能还需要改
						//resetValue();//发布成功之后清除文本框
					}else{
						alert("博客修改失败！");
					}
				},"json")
		}
	}
	
</script>
</head>
<body style="margin:10px;">

<div id="p" class="easyui-panel" title="修改博客" style="padding:10px;">
	<table cellspacing="20px">
		<tr>
			<td width="80px;">博客标题:</td>
			<td>
				<input type="text" id="title" name="title" style="width:400px;"/>
			</td>
		</tr>
		<tr>
			<td>所属类别:</td>
			<td><!-- 这个下拉框不可编辑,下拉框的高度根据里面内容的多少自动调整 -->
				<select class="easyui-combobox" style="width:154px;" id="blogTypeId" name="blogType.id" editable="false" panelHeight="auto">
					<option value="">请选择博客类别...</option>
					<c:forEach var="blogType" items="${blogTypeCountList }">
						<option value="${blogType.id }">${blogType.typeName }</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td valign="top">博客内容：</td><!-- valign="top"让这个处于上面 -->
			<td>
				<script id="editor" name="content" type="text/plain" style="width:100%;height:500px;"></script><!-- 里面写的是编辑的内容 -->
			</td>
		</tr>
		<tr>
			<td>关键字：</td>
			<td>
				<input type="text" id="keyWord" name="keyWord" style="width:400px;"/>&nbsp;(多个关键字之间用空格隔开)
			</td>
		</tr>
		<tr>
			<td></td>
			<td>
				<a href="javascript:submitData()" class="easyui-linkbutton" data-options="iconCls:'icon-submit'">发布博客</a>
			</td>
		</tr>
	</table>
</div>

<!-- 实例化编辑器 -->
<script type="text/javascript">
	var ue= UE.getEditor('editor');//没有这一步，无法生成编辑器
	
	ue.addListener("ready",function(){//必须添加同步监听才能够在ue中加载出博客的内容
		//通过ajax请求数据
		UE.ajax.request("${pageContext.request.contextPath}/admin/blog/findById.do",
				{
					method:"post",//类似于$.post
					async:false,//是否异步，这里采用同步的方式，必须要添加
					//post请求时需要带一个id参数，这里采用内置对象param来获取id
					data:{"id":"${param.id}"},
					onsuccess:function(result){//回调
						result=eval("("+result.responseText+")");//将json字符串转换为json对象
						//数据回显
						$("#title").val(result.title);
						$("#keyWord").val(result.keyWord);
						//对下拉框进行赋值
						$("#blogTypeId").combobox("setValue",result.blogType.id);
						//对编辑器赋值
						UE.getEditor('editor').setContent(result.content);
					}
				});
	})
</script>
</body>
</html>