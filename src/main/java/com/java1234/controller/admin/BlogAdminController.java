package com.java1234.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.java1234.entity.Blog;
import com.java1234.entity.PageBean;
import com.java1234.lucene.BlogIndex;
import com.java1234.service.BlogService;
import com.java1234.util.ResponseUtil;
import com.java1234.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

//����Ա����controller��
@Controller
@RequestMapping("/admin/blog")
public class BlogAdminController {

	@Resource
	private BlogService blogService;
	
	private BlogIndex blogIndex=new BlogIndex();
	
	//��Ӻ��޸�
	@RequestMapping("/save")//һ��Ҫ���ӳ��·��
	public String save(Blog blog,HttpServletResponse response)throws Exception {//ʹ��response����ʱ�򽫷�����Ϣ��װ��json��������ǰ��ҳ����
		int resultTotal=0;//���ؼ�¼�����������0�������ǲ����ɹ�
		if(blog.getId()==null) {//���
			resultTotal=blogService.add(blog);
			blogIndex.addIndex(blog);
		}else {//�޸�
			resultTotal=blogService.update(blog);//��Ӻ��޸ľ����һ��id
			//����ͬʱ��Ҫ��������
			blogIndex.updateIndex(blog);
		}
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
	 * ��ҳ��ѯ������Ϣ
	 * @param page easyUI�е����/��һҳ�ͻᴫ����̨page��rows��������ǰҳ��ÿҳ��������
	 * @param rows
	 * @param s_blog ���ڽ��ղ�ѯ��Ϣ�Ķ���
	 * @param response ajax��Ҫ
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public String list(@RequestParam(value="page",required=false)String page,@RequestParam(value="rows",required=false)String rows,Blog s_blog,HttpServletResponse response)throws Exception {
		PageBean pageBean=new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("title", StringUtil.formatLike(s_blog.getTitle()));//����ֱ�Ӳ������ݿ��ģ����ѯ
		map.put("start", pageBean.getStart());
		map.put("size", pageBean.getPageSize());
		List<Blog> blogList=blogService.list(map);
		Long total=blogService.getTotal(map);//��ȡ�ܼ�¼��
		JSONObject result=new JSONObject();
		JsonConfig jsonConfig=new JsonConfig();
		//��ĳ������ת������һ������,���ý�blog�е�ʱ������ת�����ַ������ͣ��������blogListת����jsonArray����
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd"));
		JSONArray jsonArray=JSONArray.fromObject(blogList, jsonConfig);//��blogListת����jsonArray
		//����Ҫ��������easyUI���ܽ���
		result.put("rows", jsonArray);
		result.put("total", total);
		ResponseUtil.write(response,result);
		return null;
	}
	
	/**
	 * ������Ϣɾ��
	 * @param ids
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	public String delete(@RequestParam(value="ids",required=false)String ids,HttpServletResponse response)throws Exception{
		String[] idsStr=ids.split(",");
		for(int i=0;i<idsStr.length;i++) {
			blogService.delete(Integer.parseInt(idsStr[i]));
			//ɾ������֮��ͬʱҲҪɾ����������Ȼ��ʱ����ҵ�ʱ���Ѿ�ɾ�����ò��������ܲ��ҳ���
			blogIndex.deleteIndex(idsStr[i]);
		}
		JSONObject result=new JSONObject();
		result.put("success", true);
		ResponseUtil.write(response, result);
		return null;
	}
	
	//�����id��һ����Ҫ�����Բ���required=false,����ֻ����һ��
	//��Ϊ�޸ĵ�ʱ����Ҫ��һ���µı༭���ڲ��ҰѲ��͵���Ϣȫ�����Ե��༭�����У�������Ҫͨ��id���Ҳ���
	@RequestMapping("/findById")
	public String findById(@RequestParam(value="id")String id,HttpServletResponse response)throws Exception{
		Blog blog=blogService.findById(Integer.parseInt(id));
		JSONObject result=JSONObject.fromObject(blog);//��object����ת��Ϊjson����
		ResponseUtil.write(response, result);
		return null;
	}
}
