package com.cat.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cat.common.vo.SysResult;
import com.cat.order.pojo.Order;
import com.cat.order.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/order")
public class OrderController {
	private static final ObjectMapper objectMapper = new ObjectMapper();
	@Autowired
	private OrderService orderService;
	@RequestMapping("/create")
	@ResponseBody
	public SysResult saveOrder(String orderJSON){
		try {
			Order order = objectMapper.readValue(orderJSON, Order.class);
			String orderId = orderService.saveOrder(order);
			System.out.println(orderId);
			return SysResult.oK(orderId);
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, "新增失败");
		}
	}
	@RequestMapping("/query/{orderId}")
	@ResponseBody
	public Order findOrderById(@PathVariable String orderId){
		return orderService.findOrderById(orderId);
	}
}
