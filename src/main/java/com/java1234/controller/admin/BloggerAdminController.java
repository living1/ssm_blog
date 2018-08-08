package com.java1234.controller.admin;

import java.io.File;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.java1234.entity.Blogger;
import com.java1234.service.BloggerService;
import com.java1234.util.CryptographyUtil;
import com.java1234.util.DateUtil;
import com.java1234.util.ResponseUtil;

import net.sf.json.JSONObject;

/**
 * 管理员博主controller层
 * @author living
 *
 */
@Controller
@RequestMapping("/admin/blogger")
public class BloggerAdminController {

	@Resource
	private BloggerService bloggerService;
	
	/**
	 * 查询博主信息
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/find")
	public String find(HttpServletResponse response) throws Exception{
		Blogger blogger=bloggerService.find();
		JSONObject jsonObject=JSONObject.fromObject(blogger);//将blogger转换成json对象
		ResponseUtil.write(response, jsonObject);
		return null;
	}
	/**
	 * 修改博主信息
	 * @param imageFile MultipartFile封装上传文件
	 * @param blogger
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save")
	public String save(@RequestParam("imageFile") MultipartFile imageFile,Blogger blogger,HttpServletResponse response,HttpServletRequest request)throws Exception{
		if(!imageFile.isEmpty()) {//如果用户没有上传新的头像那就默认使用原来的头像
			String filePath=request.getServletContext().getRealPath("/");//获取服务器路径/根路径
			//生成图片在项目中的保存文件名imageName
			//imageFile.getOriginalFilename()获取用户上传的文件名 .split("\\.")[1];获取这个文件名的后缀，先用分隔符分开
			String imageName=DateUtil.getCurrentDateStr()+"."+imageFile.getOriginalFilename().split("\\.")[1];
			//文件上传，但是这里只会上传到eclipse的插件中，项目中找不到这个文件夹和文件，但是在真实的环境中是可以看得到的
			imageFile.transferTo(new File(filePath+"static/userImages/"+imageName));//将文件上传到这个指定目录下的文件中
			//更新数据库
			blogger.setImageName(imageName);
		}
		int resultTotal=bloggerService.update(blogger);
		StringBuffer result=new StringBuffer();
		if(resultTotal>0) {//直接封装一个js到前台执行
			result.append("<script language='javascript'>alert('修改成功！');</script>");
		}else {
			result.append("<script language='javascript'>alert('修改失败！');</script>");
		}
		ResponseUtil.write(response, result);//这个就是将封装好的数据传到前台
		return null;
	}
	
	/**
	 * 修改博主密码
	 * @param newPassword
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/modifyPassword")//因为是ajax请求所以要加上response
	public String modifyPassword(String newPassword,HttpServletResponse response)throws Exception{
		Blogger blogger=new Blogger();
		blogger.setPassword(CryptographyUtil.md5(newPassword, "java1234"));//新的密码也是要经过md5加密之后才存入数据库中
		int resultTotal=bloggerService.update(blogger);
		JSONObject result=new JSONObject();
		if(resultTotal>0) {
			result.put("success", true);
		}else {
			result.put("success", false);
		}
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * 注销/安全退出
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/logout")
	public String logout()throws Exception{
		//使用shiro的方法,底层还是将session去掉
		SecurityUtils.getSubject().logout();
		return "redirect:/login.jsp";//重定向到后台登录界面
	}
}
