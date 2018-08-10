package com.cat.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cat.dubbo.pojo.Item;
import com.cat.dubbo.service.SearchService;

@Controller
public class SearchController {
	@Autowired
	private SearchService searchService;
	//url:/search.html?q=小米
	@RequestMapping("/search")
	public String findItemByKey(@RequestParam("q")String keyword,Model model){
		//由于get提交中文必定乱码
		try {
			keyword = new String(keyword.getBytes("ISO-8859-1"), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		//根据关键词查询solr获取数据
		List<Item> itemList = searchService.findItemByKey(keyword);
		//返回给页面关键字
		model.addAttribute("query", keyword);
		model.addAttribute("itemList", itemList);
		return "search";
	}
}
