package com.cat.sso.service;

import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cat.sso.mapper.UserMapper;
import com.cat.sso.pojo.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.JedisCluster;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private JedisCluster jedisCluster;
	private static final ObjectMapper objectMapper = new ObjectMapper();
	@Override
	public Boolean findCheckUser(String param, Integer type) {
		//Type为类型，可选参数1 username、2 phone、3 email
		String cloumn = null;
		switch (type) {
			case 1: cloumn = "username";break;
			case 2: cloumn = "phone";break;
			case 3: cloumn = "email";break;
		}
		// 1 0
		int count = userMapper.findCheckUser(param,cloumn);
		return count == 1? true:false;
	}
	@Override
	public String saveUser(User user) {
		//将数据准备完整
		user.setCreated(new Date());
		user.setUpdated(user.getCreated());
		//将密码进行加密处理
		String md5Password = DigestUtils.md5Hex(user.getPassword());
		user.setPassword(md5Password);
		//修改bug 邮箱
		user.setEmail(user.getPhone());
		//执行insert操作
		userMapper.insert(user);
		return user.getUsername();
	}
	@Override
	public String findUserByUP(String username, String password) {
		//根据用户名和加密后的密码查询用户信息
		String md5Password = DigestUtils.md5Hex(password);
		User user = userMapper.selectUserByUP(username,md5Password);
		//判断user是否为null
		if(user!=null){
			//准备ticket信息
			String ticket = "JT_TICKET_"+System.currentTimeMillis()+user.getUsername();
			//加密ticket
			String md5Ticket = DigestUtils.md5Hex(ticket);
			try {
				String userJSON = objectMapper.writeValueAsString(user);
				//将数据存入redis中
				jedisCluster.set(md5Ticket, userJSON);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
