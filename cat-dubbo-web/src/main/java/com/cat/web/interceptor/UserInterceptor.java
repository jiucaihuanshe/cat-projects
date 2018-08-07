package com.cat.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.cat.common.util.CookieUtils;
import com.cat.web.pojo.User;
import com.cat.web.util.UserThreadLocal;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.JedisCluster;

//用来获取用户信息	HandlerInterceptor是SpringMVC中拦截器的接口
public class UserInterceptor implements HandlerInterceptor {
	@Autowired
	private JedisCluster jedisCluster;
	private static final ObjectMapper objectMapper = new ObjectMapper();
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//1.获取Cookie得到ticket信息
		String ticket = CookieUtils.getCookieValue(request, "JT_TICKET");
		//2.判断Cookie中是否有值
		if(!StringUtils.isEmpty(ticket)){
			//3.如果ticket不为null,通过缓存查询用户信息
			String userJSON = jedisCluster.get(ticket);
			//4.判断缓存数据是否为null	原因: 浏览器一直保存着cookie,redis中有缓存策略,可能会删除过期的key.所以需要判断
			if(!StringUtils.isEmpty(userJSON)){
				//5.表示含有数据不为null
				User user = objectMapper.readValue(userJSON, User.class);
				//6.User对象如何存储，才能在Cart中获取user信息
				//通过ThreadLocal实现数据的传递
				UserThreadLocal.setUser(user);
				//放行转向
				return true;
			}
		}
		//用户没有登录进行页面的跳转
		response.sendRedirect("/user/login.html");
		return false;//false表示拦截,不会放行目标页面
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
