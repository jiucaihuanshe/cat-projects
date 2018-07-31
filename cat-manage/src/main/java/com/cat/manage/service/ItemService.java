package com.cat.manage.service;

import java.util.List;

import com.cat.common.vo.EasyUIResult;
import com.cat.manage.pojo.Item;
import com.cat.manage.pojo.ItemDesc;

public interface ItemService {
	List<Item> findAll();
	//分页查询数据
	EasyUIResult findItemByPage(int page, int rows);
	//根据id查询叶子子类的名称name
	String findItemNameById(Long itemId);
	//新增商品
	void saveItem(Item item,String desc);
	
	String queryItemCatNameByItemId(Long itemId);
	//修改商品
	void updateItem(Item item,String desc);
	//删除商品
	void deleteItems(Long[] ids);
	//商品上架
	void updateStatus(Long[] ids, int status);
	//查询描述信息
	ItemDesc findItemDesc(Long itemId);
	
	Item findItemById(Long itemId);
}
