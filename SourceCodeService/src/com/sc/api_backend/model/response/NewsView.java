package com.sc.api_backend.model.response;

import java.util.List;

import com.sc.db.news.model.News;

public class NewsView {
	List<News> newsList;
	String month;
	public List<News> getNewsList() {
		return newsList;
	}
	public void setNewsList(List<News> newsList) {
		this.newsList = newsList;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}

}
