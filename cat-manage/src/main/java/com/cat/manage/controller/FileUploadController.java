package com.cat.manage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cat.common.vo.PicUploadResult;
import com.cat.manage.service.FileUploadService;
@Controller
public class FileUploadController {
	/**
	 * 说明:文件上传时需要通过文件上传解析器 才能完成
	 * 所以获取文件对象时也需要通过MultipartFile接口完成
	 * @param uploadFile
	 * @return
	 */
	@Autowired
	private FileUploadService fileUploadService;
	@RequestMapping("/pic/upload")
	@ResponseBody
	public PicUploadResult uploadFile(MultipartFile uploadFile){
		return fileUploadService.uploadFile(uploadFile);
	}
}
