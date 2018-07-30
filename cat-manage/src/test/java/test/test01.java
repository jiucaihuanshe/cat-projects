package test;

import org.junit.Test;

import redis.clients.jedis.Jedis;

public class test01 {
	//@Test
	public void test01(){
		Jedis jedis = new Jedis("192.168.56.136", 6379);
		jedis.set("b", "tomcat");
		System.out.println(jedis.get("b"));
	}
}
