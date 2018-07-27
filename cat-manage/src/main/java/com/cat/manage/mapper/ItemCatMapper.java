package com.cat.manage.mapper;

import com.cat.common.mapper.SysMapper;
import com.cat.manage.pojo.ItemCat;

public interface ItemCatMapper extends SysMapper<ItemCat>{
	/*
	 * 除非有特定的业务,否则不需要写方法
	 * 规则：使用通用Mapper通常适用于单表操作
	 * 如果是多表关联需要自己编写sql
	 */
}
