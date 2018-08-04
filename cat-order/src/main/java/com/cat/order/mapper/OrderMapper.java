package com.cat.order.mapper;

import java.util.Date;

import com.cat.common.mapper.SysMapper;
import com.cat.order.pojo.Order;

public interface OrderMapper extends SysMapper<Order>{
	//将状态由未支付1改为交易关闭6
	void updateStatusByDate(Date time);
}
