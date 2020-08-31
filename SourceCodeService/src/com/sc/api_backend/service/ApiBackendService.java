package com.sc.api_backend.service;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.sc.api_backend.model.login.LoginOutput;
import com.sc.api_backend.model.request.DeleteAdmin;
import com.sc.api_backend.model.request.EditAdminRequest;
import com.sc.api_backend.model.request.InsertAdminRequest;
import com.sc.api_backend.model.request.LogRequest;
import com.sc.api_backend.model.request.LoginBackendRequest;
import com.sc.api_backend.model.request.NewsRequest;
import com.sc.api_backend.model.request.OpenData;
import com.sc.api_backend.model.request.UpdataPwRequest;
import com.sc.api_backend.model.response.ContentView;
import com.sc.api_backend.model.response.NewsView;
import com.sc.api_backend.model.response.OperatingLogView;
import com.sc.db.admininfo.model.AdminInfo;
import com.sc.db.content.model.Content;
import com.sc.db.news.model.News;

public interface ApiBackendService {
	public List<News> queryNewsListOpenData(OpenData openData);
	
	public LoginOutput loginBackend(LoginBackendRequest loginBackendRequest);
	public List<AdminInfo> queryAdminList();
	public int insertAdminInfo(InsertAdminRequest insertAdminRequest, Integer adminId);
	public int updateAdminInfo(EditAdminRequest editAdminRequest, Integer adminId);
	public int removeAdminInfo(DeleteAdmin deleteAdmin, Integer adminId);
	public int updataPassWord(UpdataPwRequest updataPwRequest, Integer adminId);
	
	//--- Content ----  
	public ContentView queryContent(Content content);
	public int saveContent(ContentView contentView, Integer adminId);
	
	//--- News ----  
	public int addNews(News news , Integer adminId);
	public List<News> queryNewsList(NewsRequest newsRequest);
	public News queryNews(NewsRequest newsRequest);
	public int updateNews(News news , Integer adminId);
	public int deleteNews(NewsRequest newsRequest, Integer adminId);
	public List<NewsView> queryNewsViewList(NewsRequest newsRequest);
	
	public List<OperatingLogView> queryLogList(LogRequest logRequest);
	
}
