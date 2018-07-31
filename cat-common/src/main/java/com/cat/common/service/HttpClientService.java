package com.cat.common.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HttpClientService {
	@Autowired(required=false)
	private CloseableHttpClient httpClient;
	@Autowired(required=false)	//请求的参数
	private RequestConfig requestConfig;
	/**
	 * 编辑工具类的思路
	 * 1.编辑Get()工具类
	 * get请求如何添加参数  www.jt.com/addUser?id=1&name=tom
	 * get请求如何解决获取参数后的乱码问题设定字符集
	 * 应该重构多个get方法满足不同的需求
	 * 2.编辑POST()工具类
	 * post请求如何传递参数表单提交时采用post请求
	 * post乱码相对而言比较好解决
	 * 满足不用的post需求
	 * @param uri
	 * @param params
	 * @param encode
	 * @return
	 * @throws Exception
	 */
	public String doGet(String uri,Map<String, String> params,String encode) throws Exception{
		//1.判断是否含有请求参数
		if(params!=null){
			//定义拼接参数的工具类
			URIBuilder builder = new URIBuilder(uri);
			//循环遍历Map获取key和value
			for(Map.Entry<String, String> entry : params.entrySet()){
				builder.setParameter(entry.getKey(), entry.getValue());
			}
			//www.jt.com/addUser?id=1&name=tom
			uri = builder.build().toString();
			System.out.println(uri);
		}
		//2.定义字符集编码
		if(null == encode){
			//设定默认的字符集编码
			encode = "UTF-8";
		}
		//3.定义get请求
		HttpGet httpGet = new HttpGet(uri);
		httpGet.setConfig(requestConfig);//定义请求的设置
		//4.准备发出请求
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(httpGet);
			if(response.getStatusLine().getStatusCode() == 200){
				String result = EntityUtils.toString(response.getEntity(),encode);
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String doGet(String uri,Map<String, String> params) throws Exception{
		return doGet(uri, params, null);
	}
	public String doGet(String uri) throws Exception{
		return doGet(uri, null, null);
	}
	
	public String doPost(String uri,Map<String, String> params,String encode) throws Exception{
		//1.定义post提交方式
		HttpPost httpPost = new HttpPost(uri);
		httpPost.setConfig(requestConfig);
		//2.如果有提交参数则进行处理{id:1,name:tom,xxx}
		if(params!=null){
			//3.定义数据封装的集合
			List<NameValuePair> pairList = new ArrayList<>();
			//4.为集合赋值
			for(Map.Entry<String, String> entry : params.entrySet()){
				pairList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			//设定字符集
			if(null == encode){
				encode = "UTF-8";
			}
			//5.定义form表单对象
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(pairList, encode);
			//6.将form表单对象添加到post对象中
			httpPost.setEntity(formEntity);
		}
		CloseableHttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(httpPost);
			if(httpResponse.getStatusLine().getStatusCode()==200){
				//证明请求成功
				String result = EntityUtils.toString(httpResponse.getEntity(), encode);
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String doPost(String uri,Map<String, String>params) throws Exception{
		return doPost(uri, params, null);
	}
	public String doPost(String uri) throws Exception{
		return doPost(uri, null, null);
	}
}
