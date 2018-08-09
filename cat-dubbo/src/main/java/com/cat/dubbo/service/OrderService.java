package com.cat.dubbo.service;

import com.cat.dubbo.pojo.Order;

public interface OrderService {
	//新增
	String saveOrder(Order order);
	//获取order
	Order findOrderById(String orderId);

}
