package com.cat.manage.service;

import java.util.List;

import com.cat.common.vo.ItemCatResult;
import com.cat.manage.pojo.ItemCat;

public interface ItemCatService {
	//定义业务方法
	List<ItemCat> findItemCat();
	//查询菜单
	List<ItemCat> findItemCatByParentId(Long parentId);
	//查询所有商品的分类信息
	ItemCatResult findItemCatAll();
	//引入缓存技术，保存所有分类信息
	ItemCatResult findItemCatAllByCache();
}
