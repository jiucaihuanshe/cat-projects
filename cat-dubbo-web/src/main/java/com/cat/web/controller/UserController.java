package com.cat.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cat.common.util.CookieUtils;
import com.cat.common.vo.SysResult;
import com.cat.web.pojo.User;
import com.cat.web.service.UserService;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private JedisCluster jedisCluster;
	@RequestMapping("/{param}")
	public String module(@PathVariable String param){
		// /user/register.html
		// /user/login.html	实现登录和注册页面跳转
		//转向用户登录和注册页面
		return param;
	}
	
	//用户注册
	//url:http://www.jt.com/service/user/doRegister
	@RequestMapping("/doRegister")
	@ResponseBody
	public SysResult saveUser(User user){
		String username = null;
		try {
			username = userService.saveUser(user);
			//用户名不为空时
			if(!StringUtils.isEmpty(username)){
				//正确返回数据
				return SysResult.oK(username);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201, "注册失败",user.getUsername());
	}
	
	//用户登录
	//url:http://www.jt.com/service/user/doLogin?r=0.5528378697323688
	//通过login.jsp检测登录的username和password是否正确
	@RequestMapping("/doLogin")
	@ResponseBody
	public SysResult doLogin(String username,String password,HttpServletRequest request,HttpServletResponse response){
		//判断用户名和密码是否为null
		if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
			return SysResult.build(201, "用户名或密码不能为空");
		}
		//当用户名和密码是正确的
		try {
			//获取用户的ticket
			String ticket = userService.findUserByUP(username,password);
			//ticket不为空
			if(!StringUtils.isEmpty(ticket)){
				CookieUtils.setCookie(request, response, "JT_TICKET", ticket);
				return SysResult.oK(ticket);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201, "用户登录失败");
	}
	
	//用户退出
	/**
	 * 1.cookie中获取ticket信息
	 * 2.删除redis缓存
	 * 3.删除cookie信息
	 * 4.跳转页面到系统欢迎页面
	 */
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response){
		String ticket = CookieUtils.getCookieValue(request, "JT_TICKET");
		jedisCluster.del(ticket);
		CookieUtils.deleteCookie(request, response, ticket);
		//通过重定向的方式返回系统首页
		return "redirect:/index.html";
	}
}
