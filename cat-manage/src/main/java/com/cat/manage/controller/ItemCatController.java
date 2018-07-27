package com.cat.manage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cat.common.vo.SysResult;
import com.cat.manage.pojo.Item;
import com.cat.manage.pojo.ItemCat;
import com.cat.manage.service.ItemCatService;
import com.cat.manage.service.ItemService;

@Controller
@RequestMapping("/item")
public class ItemCatController {
	@Autowired
	private ItemCatService itemCatService;
	@Autowired
	private ItemService itemService;
	/*@RequestMapping("/itemcat/findItemCat")
	@ResponseBody
	public List<ItemCat> findItemCat(){
		return itemCatService.findItemCat();
	}*/
	
	//url:http://localhost:8091/item/cat/list
	/* 实现思路:
	 * 	1.当第一次展开节点信息时由于没有点击节点信息所以不会传递Id值
	 * 这时需要一个默认值id=0用来加载一级菜单
	 *  2.如果鼠标点击一级菜单,会将当前的一级菜单的Id值进行传递
	 *  查询当前一级菜单下的所有二级菜单
	 *  3.查询三级菜单的步骤和二级菜单一致.
	 */
	@RequestMapping("/cat/list")
	@ResponseBody
	public List<ItemCat> findItemCat(@RequestParam(value="id",defaultValue="0")Long parentId){
		List<ItemCat> itemCatList = itemCatService.findItemCatByParentId(parentId);
		return itemCatList;
	}
	
	//http://localhost:8091/item/save
	@RequestMapping("/save")
	@ResponseBody
	public SysResult saveItem(Item item,String desc){
		try {
			itemService.saveItem(item,desc);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, "新增商品失败");
		}
	}
}
