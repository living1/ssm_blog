<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><!-- c标签，引入这个才能使用c:forEach -->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>修改个人信息页面</title>
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
		//var nickName=$("#id").val();
		//var nickName=$("#userName").val();
		//只需要获取这是三个数据，因为这三个数据是必须的不能为空，获取他们用来做判断，其他的不需要，如果这三个符合要求就可以提交了
		var nickName=$("#nickName").val();
		var sign=$("#sign").val();
		//var imageFile=$("#imageFile").val();
		var profile=UE.getEditor('profile').getContent();
		if(nickName==null || nickName==''){
			alert("请输入昵称!");
		}else if(sign==null || sign==''){
			alert("请输入个性签名!");
		}else if(profile==null || profile==''){
			alert("请输入个人简介!");
		}else{//所有验证都通过就提交表单
			$("#pF").val(profile);//将编辑器中的值赋给input输入框，这样就能提交了，然后pF的name是profile，刚好也能对应后台
			$("#form1").submit();
		}
	}
</script>
</head>
<body style="margin:10px;">

<div id="p" class="easyui-panel" title="修改个人信息" style="padding:10px;">
<!-- enctype="multipart/form-data"传文件的时候必须要加上这个,采用流的形式 -->
	<form id="form1" action="${pageContext.request.contextPath }/admin/blogger/save.do" method="post" enctype="multipart/form-data">
		<table cellspacing="20px">
			<tr>
				<td width="80px;">用户名:</td>
				<td><!-- 用户名不能修改，所以采用只读的方式 -->
				<!-- id是隐藏域，这里是用于提交，但是不需要让用户看见 -->
					<input type="hidden" id="id" name="id" value="${currentUser.id }"/>
					<input type="text" id="userName" name="userName" value="${currentUser.userName }" style="width:200px;" readonly="readonly"/>
				</td>
			</tr>
			<tr>
				<td>昵称:</td><!-- 这些能修改的值最好是从后台抓取而不是从session中读取，否则修改了数据之后数据库中的值虽然改变了，但是session中的值不会立马改变(必须重新登录(不是刷新)才会变动),更新的数据不能实时显示出来 -->
				<td>
					<input type="text" id="nickName" name="nickName" value="${currentUser.nickName }" style="width:200px;"/>
				</td>
			</tr>
			<tr>
				<td>个性签名：</td><!-- valign="top"让这个处于上面 -->
				<td>
					<input type="text" id="sign" name="sign" value="${currentUser.sign }" style="width:400px;"/>
				</td>
			</tr>
			<tr>
				<td>个人头像：</td>
				<td><!-- 这个文件到后台需要重新处理，所以不能和blogger的属性重名 -->
					<input type="file" id="imageFile" name="imageFile"/>
				</td>
			</tr>
			<tr>
				<td valign="top">个人简介：</td><!-- valign="top"让这个处于上面 -->
				<td><!-- 这个编辑器可以提交，但是还是按照峰哥的方法提交 -->
				<!-- 这个ueditor编辑器只能通过监听来设置数据，不能直接设置数据 -->
				<!-- 这里没有设置name属性，如果设置了两个name=profile那么到时候后台会得到两倍的原始数据，比如填的是x，后台接收到的就是xx -->
					<script id="profile" type="text/plain" style="width:100%;height:500px;"></script><!-- 里面写的是编辑的内容 -->
					<!-- 通过一个隐藏域来进行提交，name="profile"因为到时候这个要对接到后台的profile字段 -->
					<input type="hidden" id="pF" name="profile"/>
				</td>
			</tr>
			<tr>
				<td></td>
				<td>
					<a href="javascript:submitData()" class="easyui-linkbutton" data-options="iconCls:'icon-submit'">提交</a>
				</td>
			</tr>
		</table>
	</form>
</div>

<!-- 实例化编辑器 -->
<script type="text/javascript">
//注意这个id取值，哪里要实例化成ue就用那个id
	var ue= UE.getEditor('profile');//没有这一步，无法生成编辑器
	
	ue.addListener("ready",function(){//必须添加同步监听才能够在ue中加载出博客的内容
		//通过ajax请求数据
		UE.ajax.request("${pageContext.request.contextPath}/admin/blogger/find.do",
				{
					method:"post",//类似于$.post
					async:false,//是否异步，这里采用同步的方式，必须要添加
					//post请求时需要带一个id参数，这里采用内置对象param来获取id
					data:{},
					onsuccess:function(result){//回调
						result=eval("("+result.responseText+")");//将json字符串转换为json对象
						$("#nickName").val(result.nickName);//从后台实时获取数据，保证每次更新的数据都能实时显示到页面
						$("#sign").val(result.sign);
						UE.getEditor('profile').setContent(result.profile);
					}
				});
	})
</script>
</body>
</html>