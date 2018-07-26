package com.cat.manage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cat.manage.mapper.ItemMapper;
import com.cat.manage.pojo.Item;
@Service
public class ItemServiceImpl implements ItemService {
	@Autowired
	private ItemMapper itemMapper;
	@Override
	public List<Item> findAll() {
		return itemMapper.findAll();
	}

}
