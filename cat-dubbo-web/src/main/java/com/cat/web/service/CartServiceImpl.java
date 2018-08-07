package com.cat.web.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cat.common.service.HttpClientService;
import com.cat.web.pojo.Cart;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CartServiceImpl implements CartService {
	@Autowired
	private HttpClientService httpClientService;
	private static final ObjectMapper objectMapper = new ObjectMapper();
	@Override
	public List<Cart> findCartByUserId(Long userId) {
		String uri = "http://cart.jt.com/cart/query/"+userId;
		try {
			String resultJSON = httpClientService.doGet(uri);
			JsonNode jsonNode = objectMapper.readTree(resultJSON);
			String cartListJSON = jsonNode.get("data").asText();
			//得到了data的json串
			Cart[] carts = objectMapper.readValue(cartListJSON, Cart[].class);
			//将json串转化为List集合
			List<Cart> cartList = Arrays.asList(carts);
			return cartList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public void updateCartNum(Cart cart) {
		String uri = "http://cart.jt.com/cart/update/num/"+cart.getUserId()+"/"+cart.getItemId()+"/"+cart.getNum();
		try {
			httpClientService.doGet(uri);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void deleteCart(Long userId, Long itemId) {
		String uri = "http://cart.jt.com/cart/delete/"+userId+"/"+itemId;
		try {
			httpClientService.doGet(uri);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void saveCart(Cart cart) {
		String uri = "http://cart.jt.com/cart/save";
		//使用Map封装数据
		Map<String, String> map = new HashMap<>();
		map.put("userId", cart.getUserId()+"");
		map.put("itemId", cart.getItemId()+"");
		map.put("itemTitle", cart.getItemTitle()+"");
		map.put("itemImage", cart.getItemImage()+"");
		map.put("itemPrice", cart.getItemPrice()+"");
		map.put("num", cart.getNum()+"");
		try {
			httpClientService.doPost(uri,map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}