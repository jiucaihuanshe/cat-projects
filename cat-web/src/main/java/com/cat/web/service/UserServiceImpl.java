package com.cat.web.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cat.common.service.HttpClientService;
import com.cat.common.vo.SysResult;
import com.cat.web.pojo.User;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private HttpClientService httpClientService;
	private static final ObjectMapper objectMapper = new ObjectMapper();
	//返回ticket数据
	//用户登录
	@Override
	public String findUserByUP(String username, String password) {
		//httpClient
		String uri = "http://sso.jt.com/user/login";
		Map<String, String> map = new HashMap<>();
		//注意不要有空格为了网络中传递的速度更快采用简单字符传递
		map.put("u", username);
		map.put("p", password);
		try {
			//{status:200,msg:"ok",data:xxx}
			String resultJSON = httpClientService.doPost(uri,map);
			//判断数据是否有效 将其转化为SysResult对象
			SysResult sysResult = objectMapper.readValue(resultJSON, SysResult.class);
			if(sysResult.getStatus()==200){
				return (String) sysResult.getData();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//用户注册
	@Override
	public String saveUser(User user) {
		//请求sso的路径
		String uri = "http://sso.jt.com/user/register";
		//数据封装
		Map<String, String> map = new HashMap<>();
		map.put("username", user.getUsername());
		map.put("password", user.getPassword());
		map.put("phone", user.getPhone());
		map.put("email", user.getEmail());
		try {
			//经过httpClient调用返回SysResult对象。首先应该查看返回的状态码如果200表示sso一切正确
			String resultJSON = httpClientService.doPost(uri,map);
			SysResult sysResult = objectMapper.readValue(resultJSON, SysResult.class);
			if(sysResult.getStatus()==200){
				return (String) sysResult.getData();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
