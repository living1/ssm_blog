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
 * ����Ա����controller��
 * @author living
 *
 */
@Controller
@RequestMapping("/admin/blogger")
public class BloggerAdminController {

	@Resource
	private BloggerService bloggerService;
	
	/**
	 * ��ѯ������Ϣ
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/find")
	public String find(HttpServletResponse response) throws Exception{
		Blogger blogger=bloggerService.find();
		JSONObject jsonObject=JSONObject.fromObject(blogger);//��bloggerת����json����
		ResponseUtil.write(response, jsonObject);
		return null;
	}
	/**
	 * �޸Ĳ�����Ϣ
	 * @param imageFile MultipartFile��װ�ϴ��ļ�
	 * @param blogger
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save")
	public String save(@RequestParam("imageFile") MultipartFile imageFile,Blogger blogger,HttpServletResponse response,HttpServletRequest request)throws Exception{
		if(!imageFile.isEmpty()) {//����û�û���ϴ��µ�ͷ���Ǿ�Ĭ��ʹ��ԭ����ͷ��
			String filePath=request.getServletContext().getRealPath("/");//��ȡ������·��/��·��
			//����ͼƬ����Ŀ�еı����ļ���imageName
			//imageFile.getOriginalFilename()��ȡ�û��ϴ����ļ��� .split("\\.")[1];��ȡ����ļ����ĺ�׺�����÷ָ����ֿ�
			String imageName=DateUtil.getCurrentDateStr()+"."+imageFile.getOriginalFilename().split("\\.")[1];
			//�ļ��ϴ�����������ֻ���ϴ���eclipse�Ĳ���У���Ŀ���Ҳ�������ļ��к��ļ�����������ʵ�Ļ������ǿ��Կ��õ���
			imageFile.transferTo(new File(filePath+"static/userImages/"+imageName));//���ļ��ϴ������ָ��Ŀ¼�µ��ļ���
			//�������ݿ�
			blogger.setImageName(imageName);
		}
		int resultTotal=bloggerService.update(blogger);
		StringBuffer result=new StringBuffer();
		if(resultTotal>0) {//ֱ�ӷ�װһ��js��ǰִ̨��
			result.append("<script language='javascript'>alert('�޸ĳɹ���');</script>");
		}else {
			result.append("<script language='javascript'>alert('�޸�ʧ�ܣ�');</script>");
		}
		ResponseUtil.write(response, result);//������ǽ���װ�õ����ݴ���ǰ̨
		return null;
	}
	
	/**
	 * �޸Ĳ�������
	 * @param newPassword
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/modifyPassword")//��Ϊ��ajax��������Ҫ����response
	public String modifyPassword(String newPassword,HttpServletResponse response)throws Exception{
		Blogger blogger=new Blogger();
		blogger.setPassword(CryptographyUtil.md5(newPassword, "java1234"));//�µ�����Ҳ��Ҫ����md5����֮��Ŵ������ݿ���
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
	 * ע��/��ȫ�˳�
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/logout")
	public String logout()throws Exception{
		//ʹ��shiro�ķ���,�ײ㻹�ǽ�sessionȥ��
		SecurityUtils.getSubject().logout();
		return "redirect:/login.jsp";//�ض��򵽺�̨��¼����
	}
}
