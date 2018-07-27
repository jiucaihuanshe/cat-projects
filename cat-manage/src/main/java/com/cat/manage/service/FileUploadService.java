package com.cat.manage.service;

import org.springframework.web.multipart.MultipartFile;

import com.cat.common.vo.PicUploadResult;

public interface FileUploadService {
	PicUploadResult uploadFile(MultipartFile uploadFile);
}
