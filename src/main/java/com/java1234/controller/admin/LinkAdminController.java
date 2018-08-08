package com.java1234.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.java1234.entity.Link;
import com.java1234.entity.PageBean;
import com.java1234.service.LinkService;
import com.java1234.util.ResponseUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * ����Ա�������ӿ��Ʋ�,ֻҪ��ҪĿ���Ƕ�blogtype���в����Ķ�Ҫд���������м���Ҫ��ʵ����в����Ǿ���ע��������service
 * @author living
 *
 */
@Controller
@RequestMapping("/admin/link")
public class LinkAdminController {

	@Resource
	private LinkService linkService;

	/**
	 * ��ҳ��ѯ����������Ϣ
	 * @param page easyUI�е����/��һҳ�ͻᴫ����̨page��rows��������ǰҳ��ÿҳ��������
	 * @param rows
	 * @param response ajax��Ҫ
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public String list(@RequestParam(value="page",required=false)String page,@RequestParam(value="rows",required=false)String rows,HttpServletResponse response)throws Exception {
		PageBean pageBean=new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("start", pageBean.getStart());
		map.put("size", pageBean.getPageSize());
		List<Link> linkList=linkService.list(map);
		Long total=linkService.getTotal(map);//��ȡ�ܼ�¼��
		JSONObject result=new JSONObject();
		JSONArray jsonArray=JSONArray.fromObject(linkList);//��blogListת����jsonArray
		//����Ҫ��������easyUI���ܽ���
		result.put("rows", jsonArray);
		result.put("total", total);
		ResponseUtil.write(response,result);
		return null;
	}
	
	//��Ӻ��޸�
	@RequestMapping("/save")//һ��Ҫ���ӳ��·��
	public String save(Link link,HttpServletResponse response)throws Exception {//ʹ��response����ʱ�򽫷�����Ϣ��װ��json��������ǰ��ҳ����
		int resultTotal=0;//���ؼ�¼�����������0�������ǲ����ɹ�
		if(link.getId()==null) {//���
			resultTotal=linkService.add(link);
		}else {//�޸�
			resultTotal=linkService.update(link);//��Ӻ��޸ľ����һ��id
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
	 * ��������ɾ��
	 * @param ids
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	public String delete(@RequestParam(value="ids",required=false)String ids,HttpServletResponse response)throws Exception{
		String[] idsStr=ids.split(",");
		JSONObject result=new JSONObject();
		for(int i=0;i<idsStr.length;i++) {//��ѡ�е�ÿ������ȫ���ж�һ�Σ������ɾ�Ķ��ᱻɾ��������ɾ�Ļᱻ�����������ҷ��ظ��û�����ɾ����ԭ��
			linkService.delete(Integer.parseInt(idsStr[i]));
		}
		result.put("success", true);
		ResponseUtil.write(response, result);
		return null;
	}
}
