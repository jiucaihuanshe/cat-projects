package com.cat.web.service;

import java.util.Map;

import com.cat.web.pojo.Item;
import com.cat.web.pojo.ItemDesc;

public interface ItemService {
	//根据itemId查询商品信息
	Map<String, Object> findItemById(Long itemId);
	
	//添加缓存操作
	Map<String, Object> findItemByIdCache(Long itemId);
}
