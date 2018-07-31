package com.cat.manage.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cat.common.vo.ItemCatData;
import com.cat.common.vo.ItemCatResult;
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
	@Value("${ITEM_CAT_KEY}")
	private String ITEM_CAT_KEY;
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
	
	/**
	 * 首先查询全部的商品分类信息
	 * 通过Map将数据进行分类保存 Long代表parentId
	 * 准备返回值数据ItemCatResult对象
	 * 准备一级菜单列表集合 data
	 * dataList赋值添加一级菜单
	 * 准备二级菜单 List
	 * 将二级菜单追加到一级菜单中
	 * 准备三级菜单 List
	 * 将三级菜单追加到二级菜单中
	 */
	@Override
	public ItemCatResult findItemCatAll() {
		ItemCat itemCatTemp = new ItemCat();
		itemCatTemp.setStatus(1);
		List<ItemCat> itemCatList = itemCatMapper.select(itemCatTemp);
		Map<Long, List<ItemCat>> map = new HashMap<>();
		for(ItemCat itemCat : itemCatList){
			if(map.containsKey(itemCat.getParentId())){
				//如果存在parentId证明之前有数据,只需要做数据的追加即可
				map.get(itemCat.getParentId()).add(itemCat);
			}else{
				//表示第一次 插入数据
				List<ItemCat> tempList = new ArrayList<>();
				tempList.add(itemCat);
				map.put(itemCat.getParentId(), tempList);
			}
		}
		//通过上述的转化已经将数据存入map中。
		List<ItemCatData> itemCatDataList1 = new ArrayList<>();
		for(ItemCat itemCat1 : map.get(0L)){
			ItemCatData itemCatData1 = new ItemCatData();
			itemCatData1.setUrl("/products/"+itemCat1.getId()+".html");
			itemCatData1.setName("<a href='"+itemCatData1.getUrl()+"'>"+itemCat1.getName()+"</a>");
			
			List<ItemCatData> itemCatDataList2 = new ArrayList<>();
			for(ItemCat itemCat2 : map.get(itemCat1.getId())){
				ItemCatData itemCatData2 = new ItemCatData();
				itemCatData2.setUrl("/products/"+itemCat2.getId());
				itemCatData2.setName(itemCat2.getName());
				
				List<String> itemCatDataList3 = new ArrayList<>();
				for(ItemCat itemCat3 : map.get(itemCat2.getId())){
					itemCatDataList3.add("/produts/"+itemCat3.getId()+"|"+itemCat3.getName());
				}
				itemCatData2.setItems(itemCatDataList3);
				itemCatDataList2.add(itemCatData2);
			}
			itemCatData1.setItems(itemCatDataList2);
			itemCatDataList1.add(itemCatData1);
			if(itemCatDataList1.size()>13){
				break;
			}
		}
		ItemCatResult catResult = new ItemCatResult();
		catResult.setItemCats(itemCatDataList1);
		return catResult;
	}
	@Override
	public ItemCatResult findItemCatAllByCache() {
		ItemCatResult itemCatResult = null;
		String jsonData = jedisCluster.get(ITEM_CAT_KEY);
		try {
			if(StringUtils.isEmpty(jsonData)){
				itemCatResult = findItemCatAll();
				String jsonItemCat = objectMapper.writeValueAsString(itemCatResult);
				jedisCluster.set(ITEM_CAT_KEY, jsonItemCat);
			}else{
				itemCatResult = objectMapper.readValue(jsonData, ItemCatResult.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemCatResult;
	}
}
