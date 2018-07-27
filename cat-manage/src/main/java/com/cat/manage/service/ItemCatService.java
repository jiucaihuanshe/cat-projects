package com.cat.manage.service;

import java.util.List;

import com.cat.manage.pojo.ItemCat;

public interface ItemCatService {
	//定义业务方法
	List<ItemCat> findItemCat();
	//查询菜单
	List<ItemCat> findItemCatByParentId(Long parentId);
}
