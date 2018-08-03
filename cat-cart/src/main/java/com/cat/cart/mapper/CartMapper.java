package com.cat.cart.mapper;

import com.cat.cart.pojo.Cart;
import com.cat.common.mapper.SysMapper;

public interface CartMapper extends SysMapper<Cart>{

	void updateNum(Cart cart);

	Cart findCartByUserIdAndItemId(Cart cartTemp);

}
