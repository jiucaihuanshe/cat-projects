package com.cat.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cat.common.vo.SysResult;
import com.cat.web.pojo.Cart;
import com.cat.web.pojo.Order;
import com.cat.web.service.CartService;
import com.cat.web.service.OrderService;
import com.cat.web.util.UserThreadLocal;

@Controller
@RequestMapping("/order")
public class OrderController {
	@Autowired
	private CartService cartService;
	@Autowired
	private OrderService orderService;
	/**
	 * 转向订单确认页面
	 * url:	/order/create.html
	 */
	@RequestMapping("/create")
	public String create(Model model){
		//根据当前用户信息获取购物车信息
		Long userId = UserThreadLocal.getUser().getId();
		List<Cart> carts = cartService.findCartByUserId(userId);
		model.addAttribute("carts", carts);
		//转向订单确定页面
		return "order-cart";
	}
	
	//订单新增
	@RequestMapping("/submit")
	@ResponseBody
	public SysResult saveOrder(Order order){
		try {
			//获取userId
			Long userId = UserThreadLocal.getUser().getId();
			order.setUserId(userId);
			String orderId = orderService.saveOrder(order);
			//如果程序执行一切正确返回orderId，如果执行有问题，返回null
			if(!StringUtils.isEmpty(orderId)){
				return SysResult.oK(orderId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201, "新增失败");
	}
	
	//订单调整
	@RequestMapping("/success")
	public String success(@RequestParam("id")Long orderId,Model model){
		Order order = orderService.findOrderById(orderId);
		model.addAttribute("order", order);
		//转向成功页面
		return "success";
	}
}
