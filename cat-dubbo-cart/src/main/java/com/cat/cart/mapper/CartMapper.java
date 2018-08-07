package com.cat.cart.mapper;

import com.cat.common.mapper.SysMapper;
import com.cat.cart.pojo.Cart;

public interface CartMapper extends SysMapper<Cart>{

	void updateNum(Cart cart);

	Cart findCartByUserIdAndItemId(Cart cartTemp);

}
