package com.cat.manage.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cat.common.vo.EasyUIResult;
import com.cat.manage.mapper.ItemDescMapper;
import com.cat.manage.mapper.ItemMapper;
import com.cat.manage.pojo.Item;
import com.cat.manage.pojo.ItemDesc;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.JedisCluster;
import sun.tools.jar.resources.jar;
@Service
public class ItemServiceImpl implements ItemService {
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private ItemDescMapper itemDescMapper;
	@Autowired
	private JedisCluster jedisCluster;
	private static final ObjectMapper objectMapper = new ObjectMapper();
	@Override
	public List<Item> findAll() {
		return itemMapper.findAll();
	}
	@Override
	public EasyUIResult findItemByPage(int page, int rows) {
		//查询商品记录总数
		int total = itemMapper.findItemCount();
		//起始位置
		int begin = (page-1)*rows;
		List<Item> itemList = itemMapper.findItemByPage(begin,rows);
		return new EasyUIResult(total,itemList);
	}
	@Override
	public String findItemNameById(Long itemId) {
		return itemMapper.findItemNameById(itemId);
	}
	@Override
	public void saveItem(Item item,String desc) {
		//补充数据
		item.setStatus(1);
		item.setCreated(new Date());
		item.setUpdated(item.getCreated());
		itemMapper.insert(item);
		
		//商品描述表的新增
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(item.getId());
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(item.getCreated());
		itemDesc.setUpdated(item.getCreated());
		itemDescMapper.insert(itemDesc);
		
		//将商品信息加入redis缓存
		try {
			String jsonData = objectMapper.writeValueAsString(item);
			String key = "ITEM_"+item.getId();
			jedisCluster.set(key, jsonData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public String queryItemCatNameByItemId(Long itemId) {
		return itemMapper.queryItemCatNameByItemId(itemId);
	}
	@Override
	public void updateItem(Item item,String desc) {
		item.setUpdated(new Date());
		//动态修改
		itemMapper.updateByPrimaryKeySelective(item);
		//实现商品描述信息的修改
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(item.getId());
		itemDesc.setUpdated(item.getCreated());
		itemDesc.setItemDesc(desc);
		itemDescMapper.updateByPrimaryKeySelective(itemDesc);
		
		//商品修改后，将缓存数据删除，在重新添加
		try {
			jedisCluster.del("ITEM_"+item.getId());
			String jsonData = objectMapper.writeValueAsString(item);
			String key = "ITEM_"+item.getId();
			jedisCluster.set(key, jsonData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void deleteItems(Long[] ids) {
		//删除商品表数据
		itemMapper.deleteByIDS(ids);
		//删除商品描述信息
		itemDescMapper.deleteByIDS(ids);
		
		//商品删除时,将缓存删除
		for(Long id:ids){
			jedisCluster.del("ITEM_"+id);
		}
	}
	@Override
	public void updateStatus(Long[] ids, int status) {
		itemMapper.updateStatus(ids,status);
	}
	@Override
	public ItemDesc findItemDesc(Long itemId) {
		return itemDescMapper.selectByPrimaryKey(itemId);
	}
	@Override
	public Item findItemById(Long itemId) {
		return itemMapper.selectByPrimaryKey(itemId);
	}

}
