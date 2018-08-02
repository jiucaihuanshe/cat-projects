package com.cat.sso.service;

import com.cat.sso.pojo.User;

public interface UserService {
	//检验数据是否存在
	Boolean findCheckUser(String param, Integer type);
	//用户新增
	String saveUser(User user);
	//用户登录
	String findUserByUP(String username, String password);
}
