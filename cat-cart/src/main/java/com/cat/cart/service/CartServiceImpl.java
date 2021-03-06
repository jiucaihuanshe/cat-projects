package com.cat.cart.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cat.cart.mapper.CartMapper;
import com.cat.cart.pojo.Cart;
import com.cat.common.service.BaseService;
import com.sun.org.apache.xml.internal.resolver.Catalog;

@Service
public class CartServiceImpl extends BaseService<Cart> implements CartService {
	@Autowired
	private CartMapper cartMapper;

	@Override
	public List<Cart> findCartByUserId(Long userId) {
		Cart cart = new Cart();
		cart.setUserId(userId);
		//通过通用Mapper查询购物车信息
		return cartMapper.select(cart);
	}

	@Override
	public void updateCartNum(Cart cart) {
		cart.setUpdated(new Date());
		cartMapper.updateNum(cart);
	}

	@Override
	public void deleteCart(Long userId, Long itemId) {
		Cart cart = new Cart();
		cart.setUserId(userId);
		cart.setItemId(itemId);
		//调用父类的方法规范：调用父类的方法时最好添加super
		super.deleteByWhere(cart);
	}

	@Override
	public void saveCart(Cart cart) {
		//根据itemId和userId判断用户是否购买过商品
		Cart cartTemp = new Cart();
		cartTemp.setUserId(cart.getUserId());
		cartTemp.setItemId(cart.getItemId());
		//通过查询数据库是否含有该数据
		Cart cartDB = cartMapper.findCartByUserIdAndItemId(cartTemp);
		//判断是否还有该数据
		if(cartDB!=null){
			//证明原来是有该数据
			int count=cartDB.getNum()+cart.getNum();
			//更新商品数量
			cartDB.setNum(count);
			cartDB.setUpdated(new Date());
			//数据更新 动态更新操作
			cartMapper.updateByPrimaryKeySelective(cartDB);
		}else{
			//数据库没有该数据应该新增
			cart.setCreated(new Date());
			cart.setUpdated(cart.getCreated());
			cartMapper.insert(cart);
		}
	}
}
