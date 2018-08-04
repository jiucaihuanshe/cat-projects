package com.cat.order.service;

import com.cat.order.pojo.Order;

public interface OrderService {

	String saveOrder(Order order);

	Order findOrderById(String orderId);

}
