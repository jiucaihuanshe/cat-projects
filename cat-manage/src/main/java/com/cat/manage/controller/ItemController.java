package com.cat.manage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cat.manage.pojo.Item;
import com.cat.manage.service.ItemService;

@Controller
@RequestMapping("/item/")
public class ItemController {
	@Autowired
	private ItemService itemService;
	//url:localhost:8091/item/findAll
	@RequestMapping("findAll")
	@ResponseBody
	public List<Item> findAll(){
		return itemService.findAll();
	}
}
