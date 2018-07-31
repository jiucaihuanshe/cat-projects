package com.cat.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cat.common.service.HttpClientService;
import com.cat.web.pojo.Item;
import com.fasterxml.jackson.databind.ObjectMapper;
@Service
public class ItemServiceImpl implements ItemService {
	@Autowired
	private HttpClientService httpClientService;
	private static final ObjectMapper objectMapper = new ObjectMapper();
	//前台项目没有连接数据库,所以查询操作依赖后台
	@Override
	public Item findItemById(Long itemId) {
		String uri = "http://manage.jt.com/web/item/"+itemId;
		try {
			//通过后台查询Item对象的JSON数据
			String restJSON = httpClientService.doGet(uri);
			Item item = objectMapper.readValue(restJSON, Item.class);
			return item;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
