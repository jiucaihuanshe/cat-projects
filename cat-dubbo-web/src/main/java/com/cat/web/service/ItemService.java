package com.cat.web.service;

import java.util.Map;

import com.cat.web.pojo.Item;

public interface ItemService {
	//根据itemId查询商品信息
	Item findItemById(Long itemId);
	
	//添加缓存操作
	Item findItemByIdCache(Long itemId);
}
