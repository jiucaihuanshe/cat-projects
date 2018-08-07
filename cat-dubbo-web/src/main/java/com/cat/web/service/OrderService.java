package com.cat.web.service;

import com.cat.web.pojo.Order;

public interface OrderService {
	//新增
	String saveOrder(Order order);
	//获取order
	Order findOrderById(Long orderId);

}
