package com.cat.dubbo.service;

import com.cat.dubbo.pojo.Order;

public interface OrderService {
	//新增
	String saveOrder(Order order);
	//获取order
	Order findOrderById(String orderId);
	
	//关联查询获取数据（补充）
	Order queryByOrderId(String orderId);
}
