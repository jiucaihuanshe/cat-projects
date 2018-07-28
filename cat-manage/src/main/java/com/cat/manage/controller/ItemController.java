package com.cat.manage.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cat.common.vo.EasyUIResult;
import com.cat.common.vo.SysResult;
import com.cat.manage.pojo.Item;
import com.cat.manage.pojo.ItemDesc;
import com.cat.manage.service.ItemService;

@Controller
@RequestMapping("/item")
public class ItemController {
	@Autowired
	private ItemService itemService;
	//url:localhost:8091/item/findAll
	@RequestMapping("/findAll")
	@ResponseBody
	public List<Item> findAll(){
		return itemService.findAll();
	}
	
	//url:http://localhost:8091/item/query?page=1&rows=20
	@RequestMapping("/query")
	@ResponseBody
	public EasyUIResult findItemByPage(int page,int rows){
		return itemService.findItemByPage(page,rows);
	}
	
	//url:http://localhost:8091/item/cat/queryItemName
	@RequestMapping(value="/cat/queryItemName",produces="text/html;charset=UTF-8")
	@ResponseBody
	public String queryItemCatName(Long itemId,HttpServletResponse response){
		return itemService.findItemNameById(itemId);
	}
	
	//POST /item/update
	@RequestMapping("/update")
	@ResponseBody
	public SysResult updateItem(Item item,String desc){
		try {
			itemService.updateItem(item,desc);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, "商品修改失败");
		}
	}
	
	//POST /item/delete
	@RequestMapping("/delete")
	@ResponseBody
	public SysResult deleteItem(Long[] ids){
		try {
			itemService.deleteItems(ids);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, "商品删除失败");
		}
	}
	
	//上架url:http://localhost:8091/item/reshelf
	@RequestMapping("/reshelf")
	@ResponseBody
	public SysResult updateReshelf(Long[] ids){
		//上架
		int status = 1;
		try {
			itemService.updateStatus(ids,status);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, "商品上架失败");
		}
	}
	
	//下架url:http://localhost:8091/item/instock
	@RequestMapping("/instock")
	@ResponseBody
	public SysResult updateInstock(Long[] ids){
		//下架
		int status = 2;
		try {
			itemService.updateStatus(ids,status);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, "商品下架失败");
		}
	}
	
	//url:http://localhost:8091/item/query/item/desc/1474391957
	@RequestMapping("/query/item/desc/{itemId}")
	@ResponseBody
	public SysResult findItemDesc(@PathVariable Long itemId){
		try {
			ItemDesc itemDesc = itemService.findItemDesc(itemId);
			return SysResult.oK(itemDesc);
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, "商品描述查询失败");
		}
	}
	
	
	
	
	//url:http://localhost:8091/item/param/item/query/1474391955
	//暂时用不上
	//@RequestMapping("/param/item/query/{itemId}")
	//@ResponseBody
	public String queryItemCatNameByItemId(@PathVariable Long itemId){
		//System.out.println(itemId);
		String name = itemService.queryItemCatNameByItemId(itemId);
		//System.out.println(name);
		return name;
	}
}
