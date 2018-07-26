package com.cat.manage.controller;
/**
 * 测试spring整合
 */
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cat.manage.pojo.User;
import com.cat.manage.service.UserService;

@Controller
@RequestMapping("/user/")
public class UserController {
	@Autowired
	private UserService userService;
	@RequestMapping("findAll")
	public String findAll(Model model){
		List<User> users = userService.findAll();
		model.addAttribute("userList", users);
		return "userList";
	}
}
