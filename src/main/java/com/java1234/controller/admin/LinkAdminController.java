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
 * 管理员友情链接控制层,只要主要目的是对blogtype进行操作的都要写到这里，如果中间需要对实体进行操作那就再注入其他的service
 * @author living
 *
 */
@Controller
@RequestMapping("/admin/link")
public class LinkAdminController {

	@Resource
	private LinkService linkService;

	/**
	 * 分页查询友情链接信息
	 * @param page easyUI中点击上/下一页就会传到后台page和rows参数，当前页，每页的数据量
	 * @param rows
	 * @param response ajax需要
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
		Long total=linkService.getTotal(map);//获取总记录数
		JSONObject result=new JSONObject();
		JSONArray jsonArray=JSONArray.fromObject(linkList);//将blogList转换成jsonArray
		//必须要这样返回easyUI才能解析
		result.put("rows", jsonArray);
		result.put("total", total);
		ResponseUtil.write(response,result);
		return null;
	}
	
	//添加和修改
	@RequestMapping("/save")//一定要添加映射路径
	public String save(Link link,HttpServletResponse response)throws Exception {//使用response，到时候将返回信息封装成json流，传到前端页面中
		int resultTotal=0;//返回记录数，如果大于0才能算是操作成功
		if(link.getId()==null) {//添加
			resultTotal=linkService.add(link);
		}else {//修改
			resultTotal=linkService.update(link);//添加和修改就相差一个id
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
	 * 友情链接删除
	 * @param ids
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	public String delete(@RequestParam(value="ids",required=false)String ids,HttpServletResponse response)throws Exception{
		String[] idsStr=ids.split(",");
		JSONObject result=new JSONObject();
		for(int i=0;i<idsStr.length;i++) {//把选中的每个数据全部判断一次，最后能删的都会被删除，不能删的会被留下来，并且返回给用户不能删除的原因
			linkService.delete(Integer.parseInt(idsStr[i]));
		}
		result.put("success", true);
		ResponseUtil.write(response, result);
		return null;
	}
}
