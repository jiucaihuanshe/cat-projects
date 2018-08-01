package com.cat.manage.controller.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cat.manage.pojo.Item;
import com.cat.manage.pojo.ItemDesc;
import com.cat.manage.service.ItemService;

@Controller
@RequestMapping("/web/item")
public class WebItemController {
	@Autowired
	private ItemService itemService;
	@RequestMapping("/{itemId}")
	@ResponseBody
	public Map<String, Object> findItemById(@PathVariable Long itemId){
		Item item = itemService.findItemById(itemId);
		ItemDesc itemDesc = itemService.findItemDesc(itemId);
		Map<String, Object> map = new HashMap<>();
		map.put("item", item);
		map.put("itemDesc", itemDesc);
		return map;
	}
}
