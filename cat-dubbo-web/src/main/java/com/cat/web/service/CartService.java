package com.cat.web.service;

import java.util.List;

import com.cat.web.pojo.Cart;

public interface CartService {

	List<Cart> findCartByUserId(Long userId);

	void updateCartNum(Cart cart);

	void deleteCart(Long userId, Long itemId);

	void saveCart(Cart cart);

}
