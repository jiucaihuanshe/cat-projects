package com.cat.rabbit.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cat.dubbo.pojo.Order;
import com.cat.dubbo.pojo.OrderItem;
import com.cat.dubbo.pojo.OrderShipping;
import com.cat.rabbit.mapper.OrderItemMapper;
import com.cat.rabbit.mapper.OrderMapper;
import com.cat.rabbit.mapper.OrderShippingMapper;

@Service
public class RabbitMQService {
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderItemMapper orderItemMapper;
	@Autowired
	private OrderShippingMapper orderShippingMapper;
	
	public void saveOrder(Order order) {
		//1.准备orderId
		String orderId = order.getOrderId();
		
		//2.为Order对象补齐数据
		order.setOrderId(orderId);
		order.setCreated(new Date());
		order.setUpdated(order.getCreated());
		order.setStatus(1);	//默认为未支付状态
		orderMapper.insert(order);
		
		//3.为OrderItem补齐数据
		List<OrderItem> orderItems = order.getOrderItems();
		for(OrderItem orderItem : orderItems){
			orderItem.setOrderId(orderId);
			orderItemMapper.insert(orderItem);
		}
		
		//4.为OrderShipping补齐数据
		OrderShipping orderShipping = new OrderShipping();
		orderShipping.setOrderId(orderId);
		orderShipping.setCreated(order.getCreated());
		orderShipping.setUpdated(order.getCreated());
		orderShippingMapper.insert(orderShipping);
		
		System.out.println("消息队列执行成功！");
	}
}
