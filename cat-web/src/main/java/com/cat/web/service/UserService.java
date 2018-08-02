package com.cat.web.service;

import com.cat.web.pojo.User;

public interface UserService {
	//根据用户名和密码进行查询
	String findUserByUP(String username, String password);
	//用户注册
	String saveUser(User user);

}
