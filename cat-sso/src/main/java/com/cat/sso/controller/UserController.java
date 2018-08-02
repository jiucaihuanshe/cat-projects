package com.cat.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cat.common.vo.SysResult;
import com.cat.sso.pojo.User;
import com.cat.sso.service.UserService;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private JedisCluster jedisCluster;
	//1 username、2 phone、3 email
	//http://sso.jt.com/user/check/13556565654/2
	//?r=0.9703963460292657&callback=jsonp1533188177425&_=1533188225717
	@RequestMapping("/check/{param}/{type}")
	@ResponseBody
	public Object checkUser(@PathVariable String param,@PathVariable Integer type,String callback){
		try {
			//根据传递的参数判断数据是否存在
			Boolean result = userService.findCheckUser(param,type);
			//返回JSONP数据
			MappingJacksonValue jacksonValue = new MappingJacksonValue(SysResult.oK(result));
			jacksonValue.setJsonpFunction(callback);
			return jacksonValue;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// http://sso.jt.com/user/register
	@RequestMapping("/register")
	@ResponseBody
	public SysResult saveUser(User user){
		try {
			//用户新增
			String username = userService.saveUser(user);
			return SysResult.oK(username);
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, "用户新增失败");
		}
	}
	
	//用户登录
	//@RequestParam作用：表示参数接收时采用u的关键字获取，将获取到的value值交给username属性
	@RequestMapping("/login")
	@ResponseBody
	public SysResult doLogin(@RequestParam("u")String username,@RequestParam("p")String password){
		try {
			//获取ticket信息
			String ticket = userService.findUserByUP(username,password);
			return SysResult.oK(ticket);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201, "登录失败");
	}
	
	//登录数据的回显
	//根据ticket查询用户的json信息
	//http://sso.jt.com/user/query/+ _ticket	JSONP提交
	@RequestMapping("/query/{ticket}")
	@ResponseBody
	public Object findUserByTicket(@PathVariable String ticket,String callback){
		//获取用户的JSON数据
		String userJSON= jedisCluster.get(ticket);
		MappingJacksonValue jacksonValue = new MappingJacksonValue(SysResult.oK(userJSON));
		//设定返回的方法名称
		jacksonValue.setJsonpFunction(callback);
		return jacksonValue;
	}
}
