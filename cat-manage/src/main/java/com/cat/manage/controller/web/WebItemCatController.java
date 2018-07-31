package com.cat.manage.controller.web;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cat.common.vo.ItemCatResult;
import com.cat.manage.service.ItemCatService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/web")
public class WebItemCatController {
	@Autowired
	private ItemCatService itemCatService;
	private static final ObjectMapper objectMapper = new ObjectMapper();
	//http://manage.jt.com/web/itemcat/all?callback=category.getDataService
	//通过response对象实现回显
	/*@RequestMapping("/itemcat/all")
	public void findItemCatAll(String callback,HttpServletResponse response){
		//查询商品的全部的分类信息
		ItemCatResult itemCatResult = itemCatService.findItemCatAllByCache();
		String JSONData = null;
		try {
			JSONData = objectMapper.writeValueAsString(itemCatResult);
			response.setContentType("html/text;charset=utf-8");
			//JSONP的调用，返回值准备成JSONP的格式
			//callback(ItemCatResult的JSON串);
			String jsonPString = callback+"("+JSONData+")";
			response.getWriter().write(jsonPString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	@RequestMapping("/itemcat/all")
	@ResponseBody
	public Object findItemCatAll(String callback){
		ItemCatResult itemCatResult = itemCatService.findItemCatAllByCache();
		//用来操作JSONP对象
		MappingJacksonValue jacksonValue = new MappingJacksonValue(itemCatResult);
		//将JSONP的callback参数返回
		jacksonValue.setJsonpFunction(callback);
		return jacksonValue;
	}
}
