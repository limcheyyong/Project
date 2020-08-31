package com.sc.api_backend.model.request;

import java.util.Date;

public class NewsRequest {
	
	private Integer newsId;
	   
	private Integer isOnApp;

	private Integer isOnAppEn;
	
	private String summary;

	private String summaryEn;	

	private String newsTitle;
	
	private String startDate;
	
	private String endDate;
	
	private Integer rows;
	
	private Integer lang;  // 1:中文   2:英文
	
	public Integer getNewsId() {
		return newsId;
	}

	public void setNewsId(Integer newsId) {
		this.newsId = newsId;
	}

	public Integer getIsOnApp() {
		return isOnApp;
	}

	public void setIsOnApp(Integer isOnApp) {
		this.isOnApp = isOnApp;
	}

	public Integer getIsOnAppEn() {
		return isOnAppEn;
	}

	public void setIsOnAppEn(Integer isOnAppEn) {
		this.isOnAppEn = isOnAppEn;
	}

	public String getNewsTitle() {
		return newsTitle;
	}

	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getSummaryEn() {
		return summaryEn;
	}

	public void setSummaryEn(String summaryEn) {
		this.summaryEn = summaryEn;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public Integer getLang() {
		return lang;
	}

	public void setLang(Integer lang) {
		this.lang = lang;
	}
	
}
