package com.cat.dubbo.service;

import java.util.List;

import com.cat.dubbo.pojo.Cart;

public interface CartService {
	//根据用户id查询购物车信息
	List<Cart> findCartByUserId(Long userId);

	//修改数量
	void updateCartNum(Cart cart);
	
	//删除购物车商品
	void deleteCart(Long userId, Long itemId);

	//新增商品
	void saveCart(Cart cart);
	
}
