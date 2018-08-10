package com.cat.dubbo.service;

import java.util.List;

import com.cat.dubbo.pojo.Item;

public interface SearchService {
	List<Item> findItemByKey(String keyword);
}
