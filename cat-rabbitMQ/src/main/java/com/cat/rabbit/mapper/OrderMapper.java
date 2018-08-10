package com.cat.rabbit.mapper;

import java.util.Date;

import com.cat.common.mapper.SysMapper;
import com.cat.dubbo.pojo.Order;

public interface OrderMapper extends SysMapper<Order>{
	//将状态由未支付1改为交易关闭6
	void updateStatusByDate(Date time);

	//多表的关联查询 根据orderId查询数据库
	Order queryByOrderId(String orderId);
}
