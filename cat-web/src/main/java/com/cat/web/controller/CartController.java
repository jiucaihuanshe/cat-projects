package com.cat.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cat.web.pojo.Cart;
import com.cat.web.service.CartService;

@Controller
@RequestMapping("/cart")
public class CartController {
	@Autowired
	private CartService cartService;
	//转向购物车页面
	@RequestMapping("/show")
	public String findCartByUserId(Model model){
		Long userId = 2L;	//暂时写死
		//查询购物车列表信息
		List<Cart> cartList = cartService.findCartByUserId(userId);
		//将数据写入request域中
		model.addAttribute("cartList", cartList);
		//转向cart.jsp页面
		return "cart";
	}
	
	//修改购物车商品数量
	//http://cart.jt.com/cart/update/num/2/1474391948/100
	@RequestMapping("/update/num/{itemId}/{num}")
	public String updateCartNum(@PathVariable Long itemId,@PathVariable Integer num){
		Long userId = 2L;	//获取userId
		Cart cart = new Cart();
		cart.setUserId(userId);
		cart.setItemId(itemId);
		cart.setNum(num);
		cartService.updateCartNum(cart);
		return "redirect:/cart/show.html";
	}
	
	//删除购物车 /cart/delete/7.html
	@RequestMapping("/delete/{itemId}")
	public String deleteCart(@PathVariable Long itemId){
		Long userId = 2L;
		cartService.deleteCart(userId,itemId);
		//跳转到购物车列表页面满足springMVC的要求 *.html
		return "redirect:/cart/show.html";
	}
	
	//购物车新增 http://www.jt.com/cart/add/562379.html
	@RequestMapping("/add/{itemId}")
	public String saveCart(@PathVariable Long itemId,Cart cart){
		Long userId = 2L;
		//将数据进行封装
		cart.setUserId(userId);
		cart.setItemId(itemId);
		cartService.saveCart(cart);
		//转向购物车展现页面
		return "redirect:/cart/show.html";
	}
}
