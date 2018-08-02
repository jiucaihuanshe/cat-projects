package com.cat.sso.mapper;

import org.apache.ibatis.annotations.Param;

import com.cat.common.mapper.SysMapper;
import com.cat.sso.pojo.User;

public interface UserMapper extends SysMapper<User>{
	//查询数据是否存在将数据封装为Map
	int findCheckUser(@Param("param")String param, @Param("cloumn")String cloumn);
	//用户登录
	User selectUserByUP(@Param("username")String username, @Param("password")String md5Password);
}
