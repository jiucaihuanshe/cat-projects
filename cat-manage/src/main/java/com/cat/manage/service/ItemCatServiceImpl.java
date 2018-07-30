package com.cat.manage.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cat.manage.mapper.ItemCatMapper;
import com.cat.manage.pojo.ItemCat;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.JedisCluster;

@Service
public class ItemCatServiceImpl implements ItemCatService {
	@Autowired
	private ItemCatMapper itemCatMapper;
	@Autowired
	private JedisCluster jedisCluster;
	//定义格式转化工具
	private static final ObjectMapper objectMapper = new ObjectMapper();
	@Override
	public List<ItemCat> findItemCat() {
		//调用通用Mapper方法，如果查询全部数据则不需要设定参数直接为null
		return itemCatMapper.select(null);
	}
	@Override
	public List<ItemCat> findItemCatByParentId(Long parentId) {
		/**
		 * 通用Mapper中带参数的查询
		 * 如果需要带参数查询，只需要将参数set到具体的对象中即可。
		 * 实现思路：
		 * 通用Mapper会将对象中不为null的数据当做where条件进行查询
		 */
		//1.定义查询的key值
		String key = "ITEM_CAT_Cluster"+parentId;
		//2.根据key值查询缓存数据
		String dataJSON = jedisCluster.get(key);
		//最后定义公用的List集合
		List<ItemCat> itemCatList = new ArrayList<>();
		try {
			//3.判断返回值是否含有数据
			if(StringUtils.isEmpty(dataJSON)){
				//证明缓存中没有数据，则通过数据库查询数据
				ItemCat itemCat = new ItemCat();
				itemCat.setParentId(parentId);	//设定父级id
				itemCat.setStatus(1);	//设定为正常的数据 1
				itemCatList = itemCatMapper.select(itemCat);
				//返回的数据转化为JSON串 存入到redis中
				String jsonResult = objectMapper.writeValueAsString(itemCatList);
				jedisCluster.set(key, jsonResult);
			}else{
				//表示数据不为空需要将JSON串转化为List<ItemCat>集合对象
				ItemCat[] itemCats = objectMapper.readValue(dataJSON, ItemCat[].class);
				for(ItemCat itemCat : itemCats){
					itemCatList.add(itemCat);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return itemCatList;
	}

}
