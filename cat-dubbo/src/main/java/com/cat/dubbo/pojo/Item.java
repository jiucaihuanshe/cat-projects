package com.cat.dubbo.pojo;

import org.apache.solr.client.solrj.beans.Field;

/**
 * item对象
 */
import com.cat.common.po.BasePojo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Item extends BasePojo{
	/**
	 * Item对象的属性应该与solr中的检索关键字匹配
	 */
	@Field("id")
	private Long id;			//商品Id
	@Field("title")
	private String title;		//商品标题
	@Field("sellPoint")
	private String sellPoint;	//商品卖点
	@Field("price")
	private Long price;			//价格
	@Field("num")
	private Integer num;		//库存数量
	private String barcode;		//条形码
	@Field("image")
	private String image;		//图片 
	private Long cid;		//所属分类
	private Integer status;		//状态
	
	//为了页面需要只显示第一张图片
	private String[] images;
	public String[] getImages(){
		return image.split(",");
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSellPoint() {
		return sellPoint;
	}
	public void setSellPoint(String sellPoint) {
		this.sellPoint = sellPoint;
	}
	public Long getPrice() {
		return price;
	}
	public void setPrice(Long price) {
		this.price = price;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public Long getCid() {
		return cid;
	}
	public void setCid(Long cid) {
		this.cid = cid;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
