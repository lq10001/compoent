package com.ly.cms.vo;

import java.util.Date;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.View;

@Table("product")
public class Product{

	@Id
	@Column
	private Long id;

	@Column
	private String name;

	@Column
	private Long funcnameid;

	@Column
	private String dataurl;

	@Column
	private String author;

	@Column
	private Date adddate;

	@Column
	private Date editdate;

	@Column
	private String smallimage;

	@Column
	private String maximage;

	@Column
	private Long zan;

	@Column
	private String descript;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getFuncnameid() {
		return funcnameid;
	}

	public void setFuncnameid(Long funcnameid) {
		this.funcnameid = funcnameid;
	}

	public String getDataurl() {
		return dataurl;
	}

	public void setDataurl(String dataurl) {
		this.dataurl = dataurl;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getAdddate() {
		return adddate;
	}

	public void setAdddate(Date adddate) {
		this.adddate = adddate;
	}

	public Date getEditdate() {
		return editdate;
	}

	public void setEditdate(Date editdate) {
		this.editdate = editdate;
	}

	public String getSmallimage() {
		return smallimage;
	}

	public void setSmallimage(String smallimage) {
		this.smallimage = smallimage;
	}

	public String getMaximage() {
		return maximage;
	}

	public void setMaximage(String maximage) {
		this.maximage = maximage;
	}

	public Long getZan() {
		return zan;
	}

	public void setZan(Long zan) {
		this.zan = zan;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}
}
