package com.cat.web.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cat.common.service.HttpClientService;
import com.cat.web.pojo.Item;
import com.cat.web.pojo.ItemDesc;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.JedisCluster;
@Service
public class ItemServiceImpl implements ItemService {
	@Autowired
	private HttpClientService httpClientService;
	@Autowired
	private JedisCluster jedisCluster;
	private static final ObjectMapper objectMapper = new ObjectMapper();
	//前台项目没有连接数据库,所以查询操作依赖后台
	@Override
	public Map<String, Object> findItemById(Long itemId) {
		String uri = "http://manage.jt.com/web/item/"+itemId;
		try {
			//通过后台查询Item对象的JSON数据
			String restJSON = httpClientService.doGet(uri);
			Map<String, Object> item = objectMapper.readValue(restJSON, Map.class);
			return item;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/*@Override
	public Map<String, Object> findItemByIdCache(Long itemId) {
		*//**
		 * 1.先查询缓存，如果缓存中有数据则先将JSON数据转化为对象之后返回
		 * 2.如果缓存中没有该数据，则先远程调用，之后将数据存入缓存中。
		 *//*
		//形成唯一的标识
		String key = "ITEM_"+itemId;
		String jsonResult= jedisCluster.get(key);
		try {
			if(StringUtils.isEmpty(jsonResult)){
				//进行远程调用
				Item item = findItemById(itemId);
				String jsonData = objectMapper.writeValueAsString(item);
				jedisCluster.set(key, jsonData);
				return item;
			}else{
				Item item = objectMapper.readValue(jsonResult, Item.class);
				return item;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}*/

}
