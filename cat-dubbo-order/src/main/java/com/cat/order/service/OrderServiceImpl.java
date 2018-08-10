package com.cat.order.service;

import java.util.Date;
import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cat.dubbo.pojo.Order;
import com.cat.dubbo.pojo.OrderItem;
import com.cat.dubbo.pojo.OrderShipping;
import com.cat.dubbo.service.OrderService;
import com.cat.order.mapper.OrderItemMapper;
import com.cat.order.mapper.OrderMapper;
import com.cat.order.mapper.OrderShippingMapper;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderItemMapper orderItemMapper;
	@Autowired
	private OrderShippingMapper orderShippingMapper;
	@Autowired
	private RabbitTemplate rabbitTemplate;
	/**
	 * 思路:新增订单,由于order和orderItem和orderShipping具有关联关系.
	 * 所以一次入库三张表
	 * 实现方法:
	 * 	1.通过通用Mapper实现	能够自动的生成Sql简单快捷
	 *  2.通过Sql语句实现sql比较复杂,开发效率不高
	 * 
	 * 使用通用Mapper的形式实现
	 * 	1.准备OrderId登录用户id+当前时间戳,为三个对象赋值
	 *  2.添加创建时间和修改时间
	 *  3.Order是单个对象
	 *  4.OrderItem是List集合	1.批量插入  2.循环插入
	 *  5.OrderShipping 对象
	 */

	@Override
	public String saveOrder(Order order) {
		//1.准备orderId
		String orderId = order.getUserId()+System.currentTimeMillis()+"";
		
		//为rabbitMQ封装数据
		order.setOrderId(orderId);
		//将order信息发往消息队列中rabbitMQ中 定义路由key
		rabbitTemplate.convertAndSend("save.Order",order);
		
		
		/*//2.为Order对象补齐数据
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
		orderShippingMapper.insert(orderShipping);*/
		
		//返回orderId 用于查询操作
		return orderId;
	}
	
	//3张表一起查询,通过orderId实现
	@Override
	public Order findOrderById(String orderId) {
		//查询order表
		Order order = orderMapper.selectByPrimaryKey(orderId);
		OrderItem orderItem = new OrderItem();
		orderItem.setOrderId(orderId);
		
		//查询orderItem表
		List<OrderItem> orderItemList = orderItemMapper.select(orderItem);
		order.setOrderItems(orderItemList);
		
		//查询物流表
		OrderShipping orderShipping = orderShippingMapper.selectByPrimaryKey(orderId);
		order.setOrderShipping(orderShipping);
		return order;
	}

	//通过sql实现多表关联查询
	@Override
	public Order queryByOrderId(String orderId){
		return orderMapper.queryByOrderId(orderId);
	}
}
