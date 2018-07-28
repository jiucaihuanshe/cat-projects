package com.cat.manage.service;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * 编程思想:
 * 获取图片的名称
 * 截取图片的类型
 * 判断是否为图片格式jpg|gif|png
 * 判断是否为恶意程序
 * 定义磁盘路径和url访问路径
 * 准备以时间为界限的文件夹
 * 让图片不重名 时间毫秒值+三位数字
 * 创建文件夹
 * 开始写盘操作
 * 将数据准备好返回给客户端
 *  真实路径:
 *  	E:/jt-upload/2018/1/17/12/123.jpg
 *  虚拟路径:
 *  	http://image.jt.com/2018/1/17/12/123.jpg
 */
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cat.common.vo.PicUploadResult;
@Service
public class FileUploadServiceImpl implements FileUploadService {
	private String dirPath = "C:/tedu/workspace/jt-upload/";	//定义本地磁盘路径
							//Linux /usr/local/src/jt-upload/
	private String url = "http://image.jt.com/";	//定义url访问路径
	@Override
	public PicUploadResult uploadFile(MultipartFile uploadFile) {
		PicUploadResult result = new PicUploadResult();
		String fileName = uploadFile.getOriginalFilename();
		String fileType = fileName.substring(fileName.lastIndexOf("."));
		if(!fileType.matches("^.*(jpg|png|gif)$")){
			result.setError(1);
			return result;
		}
		try {
			BufferedImage bufferedImage = ImageIO.read(uploadFile.getInputStream());
			int width = bufferedImage.getWidth();
			int height = bufferedImage.getHeight();
			if(width==0 || height==0){
				result.setError(1);
				return result;
			}
			String datePathDir = new SimpleDateFormat("yyyy/MM/dd/HH").format(new Date());
			String millis = System.currentTimeMillis()+"";
			Random random = new Random();
			int randomNum = random.nextInt(999);
			String randomPath = millis+randomNum;
			//创建文件夹
			String LocalPath = dirPath+datePathDir;
			File file = new File(LocalPath);
			if(!file.exists()){
				file.mkdirs();
			}
			//写盘路径
			String LocalPathFile = LocalPath+"/"+randomPath+fileName;
			//文件写盘
			uploadFile.transferTo(new File(LocalPathFile));
			//准备数据返回给客户端
			result.setHeight(height+"");
			result.setWidth(width+"");
			//代表虚拟的全路径
			String urlPath = url+datePathDir+"/"+randomPath+fileName;
			result.setUrl(urlPath);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
