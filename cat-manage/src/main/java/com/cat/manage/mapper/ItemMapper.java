package com.cat.manage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cat.common.mapper.SysMapper;
import com.cat.manage.pojo.Item;

public interface ItemMapper extends SysMapper<Item>{
	List<Item> findAll();
	//查询商品记录总数
	int findItemCount();
	//分页查询数据 begin代表起始位置 rows加载数据量
	//Mybatis中只允许传递单个数据.如果需要传递多个数据时,需要进行封装一般采用
	//Map结构 添加@Param("begin");
	List<Item> findItemByPage(@Param("begin")int begin, @Param("rows")int rows);
	//查询商品分类名称
	String findItemNameById(Long itemId);
	String queryItemCatNameByItemId(Long itemId);
	//商品上架
	void updateStatus(@Param("ids")Long[] ids, @Param("status")int status);
}
