package com.sc.api_backend.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.sc.api_backend.model.response.GroupMonth;
import com.sc.api_backend.model.response.NewsView;
import com.sc.api_backend.model.response.OperatingLogView;
import com.sc.api_backend.service.ApiBackendService;
import com.sc.base.service.BaseService;
import com.sc.db.admininfo.dao.AdminInfoMapper;
import com.sc.db.admininfo.model.AdminInfo;
import com.sc.db.content.dao.ContentMapper;
import com.sc.db.content.model.Content;
import com.sc.db.contentitem.dao.ContentItemMapper;
import com.sc.db.contentitem.model.ContentItem;
import com.sc.db.news.dao.NewsMapper;
import com.sc.db.news.model.News;
import com.sc.db.operatinglog.dao.OperatingLogMapper;
import com.sc.db.operatinglog.model.OperatingLog;
import com.sc.util.EncryptUtil;
import com.sc.util.LogUtils;
import com.sc.util.OpenDataUtil;
import com.sc.util.TimerUtil;

@Service
public class ApiBackendServiceImpl extends BaseService implements ApiBackendService{
	
	@Autowired
	AdminInfoMapper adminInfoMapper;
	
	@Autowired
	ContentMapper contentMapper;
	
	@Autowired
	ContentItemMapper contentItemMapper;
	
	@Autowired
	OperatingLogMapper operatingLogMapper;
	
	@Autowired
	NewsMapper newsMapper;
	

	
	public List<News> queryNewsListOpenData(OpenData openData){
		
		List<News> newsList = OpenDataUtil.queryNewsList(openData);		
		return newsList;
	}
	
	
	@Override
	public LoginOutput loginBackend(LoginBackendRequest loginBackendRequest) {
		LoginOutput loginOutput = null;

		try {
			String md5Code = EncryptUtil.digest(loginBackendRequest.getPassword());
			try{
				loginBackendRequest.setPassword(md5Code);		
				AdminInfo adminInfo = adminInfoMapper.inspectPwd(loginBackendRequest);
				if(adminInfo != null){
					loginOutput = new LoginOutput();
				
					Date currentDate = TimerUtil.getNowDate();
					SimpleDateFormat sdf = TimerUtil.getSimpleDateFormat("yyyyMMdd");		
					loginOutput.setSecurityKey(sdf.format(currentDate));
					loginOutput.setAdminInfo(adminInfo);
				}
		    }catch(Exception e){
		    	LogUtils.error(this.getClass() , "loginBackend has error :" + e);
		    }
		}catch (Exception e) {
			LogUtils.error(getClass(), "loginBackend function encrypt md5Code has error : " + e );
		}	
		return loginOutput;
	}

	@Override
	public List<AdminInfo> queryAdminList() {
		return adminInfoMapper.queryAdminList();
	}

	@Override
	public int insertAdminInfo(InsertAdminRequest insertAdminRequest, Integer adminId) {
		int ires = 0;
		AdminInfo existAdminInfo = adminInfoMapper.selectByUserCode(insertAdminRequest.getUserCode());
		if(existAdminInfo !=null){
			return ires;
		}

		Date nowDate = TimerUtil.getNowDate();
		insertAdminRequest.setUserPw("40b9568bc9ca6e814cff0078d5c2d429");//Sc@2019!
		insertAdminRequest.setCreatedBy(adminId);
		insertAdminRequest.setCreationDate(nowDate);
		insertAdminRequest.setUpdatedBy(adminId);
		insertAdminRequest.setUpdateDate(nowDate);
		ires = adminInfoMapper.insert(insertAdminRequest);
		if(ires == 1)
			logOperating(1,insertAdminRequest.getAdminId(),1,1,insertAdminRequest.getUserName(),adminId);
		
		return ires;
	}

	private int logOperating(int tableId ,int tablePid , int operatingLogType , int operatingMode , String remark ,int adminId){
		int res = 0;
		OperatingLog operatingLog = new OperatingLog();
		operatingLog.setTableId(tableId);
		operatingLog.setTablePid(tablePid);
		operatingLog.setOperatingLogType(operatingLogType);
		operatingLog.setOperatingMode(operatingMode);
		operatingLog.setRemark(remark);
		operatingLog.setUserId(adminId);
		

		Date nowDate = TimerUtil.getNowDate();
		operatingLog.setCreationDate(nowDate);
		operatingLogMapper.insert(operatingLog);
		return res;
	}
	
	@Override
	public int updateAdminInfo(EditAdminRequest editAdminRequest ,Integer adminId) {
		int success = 0;
		try {
			Date nowDate = TimerUtil.getNowDate();
			editAdminRequest.setUpdatedBy(0);
			editAdminRequest.setUpdateDate(nowDate);
			success = adminInfoMapper.updateByPrimaryKeySelective(editAdminRequest);
	
			if(success==1)
				logOperating(1,editAdminRequest.getAdminId(),3,1,editAdminRequest.getUserName(),adminId);
			
		} catch (Exception e) {
			LogUtils.error(getClass(), "updateAdminInfo has error : " + e);
		}
		return success;
	}

	@Override
	public int removeAdminInfo(DeleteAdmin deleteAdmin,Integer adminId) {
		int res = 0 ;
		res =adminInfoMapper.deleteByPrimaryKey(deleteAdmin.getAdminId());
		if(res == 1)	
			logOperating(1,deleteAdmin.getAdminId(),2,1,deleteAdmin.getUserName(),adminId);
		
		return res ;
	}

	@Override
	public int updataPassWord(UpdataPwRequest updataPwRequest ,Integer adminId){
		int success = 0;
		AdminInfo adminInfo = new AdminInfo();
		try{
			String userPwMd5Code = EncryptUtil.digest(updataPwRequest.getUserPw());
			String newMd5Code = EncryptUtil.digest(updataPwRequest.getNewPw());
			updataPwRequest.setUserPw(userPwMd5Code);
			adminInfo =	adminInfoMapper.inspectPwdForupdataPassWord(updataPwRequest);

			if(adminInfo!=null){
				Date nowDate = TimerUtil.getNowDate();
				try {
					adminInfo.setUserPw(newMd5Code);
					adminInfo.setUpdatedBy(0);
					adminInfo.setUpdateDate(nowDate);
					adminInfoMapper.updateByPrimaryKeySelective(adminInfo);
					success =1;
				} catch (Exception e) {
					LogUtils.error(getClass(), "updataPassWord has error : " + e);
				}
			}else{
				success =2;
			}
		}catch (Exception e) {
			LogUtils.error(getClass(), "updataPassWord function encrypt md5Code has error : " + e );
		}	
		return success;
	}
	
	//--- Content --- 
	
	@Override
	public ContentView queryContent(Content content){
		ContentView  contentView= new ContentView();
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if(content.getContentType() !=null && content.getContentType() != 0)
			paramMap.put("contentType",content.getContentType());	
	
		List<Content> contentList = contentMapper.queryContentList(paramMap);
		if(contentList !=null && contentList.size()>0)
		{
			for(int i =0 ; i<contentList.size() ;i++)
			{
				if(contentList.get(i).getLanguageCode().equals("TW")){
					contentView.setContentTw(contentList.get(i));
					
					paramMap.clear();
					paramMap.put("contentId",contentList.get(i).getContentId());
					paramMap.put("itemType",1);
					
					List<ContentItem> contentItemList = contentItemMapper.queryContentItemList(paramMap);
					if(contentItemList == null || contentItemList.size() ==0)
					{
						contentItemList = new ArrayList<ContentItem>();
						
						for(int j=0 ; j<7 ; j++)
						{
							ContentItem contentItem = new ContentItem();
							contentItem.setOrderNo(j);
							contentItem.setItemValue(0);
							contentItem.setItemType(1);
							contentItemList.add(contentItem);
						}
					}
					
					contentView.setContentItemTwList(contentItemList);
				}
				else if(contentList.get(i).getLanguageCode().equals("EN"))
				{
					contentView.setContentEn(contentList.get(i));
				
					paramMap.clear();
					paramMap.put("contentId",contentList.get(i).getContentId());
					paramMap.put("itemType",1);
					
					List<ContentItem> contentItemList = contentItemMapper.queryContentItemList(paramMap);
					if(contentItemList == null  || contentItemList.size() ==0)
					{
						contentItemList = new ArrayList<ContentItem>();
						for(int j=0 ; j<7 ; j++)
						{
							ContentItem contentItem = new ContentItem();
							contentItem.setOrderNo(j);
							contentItem.setItemValue(0);
							contentItem.setItemType(1);
							contentItemList.add(contentItem);
						}
					}
					contentView.setContentItemEnList(contentItemList);
				}
			}
		}
		return contentView;
	}
	
	@Override
	public int saveContent(ContentView contentView ,Integer adminId){
		int res = 0;
		if (contentView ==null)
			return res;
		
		if(contentView.getContentTw().getContentId() != null &&contentView.getContentTw().getContentId() != 0)
		{
			res = contentMapper.updateByPrimaryKey(contentView.getContentTw());
		}else
		{
			res = contentMapper.insert(contentView.getContentTw());
		}
			
		if(contentView.getContentEn().getContentId() != null &&contentView.getContentEn().getContentId() != 0)
		{
			res = contentMapper.updateByPrimaryKey(contentView.getContentEn());
		}else
		{
			res = contentMapper.insert(contentView.getContentEn());
		}
		
		List<ContentItem> contentItenTwList = contentView.getContentItemTwList();
		contentItemMapper.deleteByContentId(contentView.getContentTw().getContentId());
		if(contentItenTwList != null && contentItenTwList.size() > 0)
		{
			for(int i =0 ; i< contentItenTwList.size() ;i++)
			{
				contentItenTwList.get(i).setContentId(contentView.getContentTw().getContentId());
				contentItemMapper.insert(contentItenTwList.get(i));
			}
		}
		
		List<ContentItem> contentItenEnList = contentView.getContentItemEnList();
		contentItemMapper.deleteByContentId(contentView.getContentEn().getContentId());
		if(contentItenEnList != null && contentItenEnList.size() > 0)
		{
			for(int i =0 ; i< contentItenEnList.size() ;i++)
			{
				contentItenEnList.get(i).setContentId(contentView.getContentEn().getContentId());
				contentItemMapper.insert(contentItenEnList.get(i));
			}
		}
		return res;
	}
	
	//----新聞----
	@Override
	public int addNews(News news ,Integer adminId){
		int res = 0;
		if(news.getNewsId() !=null ){
			return -2;
		}
		Date nowDate = TimerUtil.getNowDate();
		news.setImportDate(nowDate);
		res = newsMapper.insert(news);	
		return res;
	}
	
	public List<News> queryNewsList(NewsRequest newsRequest){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if(newsRequest.getSummary()!=null && !newsRequest.getSummary().equals(""))
			paramMap.put("summary",newsRequest.getSummary());		
		if(newsRequest.getSummaryEn()!=null && !newsRequest.getSummaryEn().equals(""))
			paramMap.put("summaryEn",newsRequest.getSummaryEn());	
		if(newsRequest.getIsOnApp()!=null)
			paramMap.put("isOnApp",newsRequest.getIsOnApp());	
		if(newsRequest.getIsOnAppEn()!=null)
			paramMap.put("isOnAppEn",newsRequest.getIsOnAppEn());
		
		if(newsRequest.getStartDate()!=null && !newsRequest.getStartDate().equals(""))
			paramMap.put("startDate",newsRequest.getStartDate());
		if(newsRequest.getEndDate()!=null && !newsRequest.getEndDate().equals(""))
			paramMap.put("endDate",newsRequest.getEndDate());
		
		List<News> newsList= newsMapper.queryNewsList(paramMap);
		return newsList;
	}
	
	public News queryNews(NewsRequest newsRequest){		
		News News = newsMapper.selectByPrimaryKey(newsRequest.getNewsId());
		return News;
	}
	
	public int updateNews(News news , Integer adminId){
		int res=0;
		res = newsMapper.updateByPrimaryKeySelective(news);
		return res;		
	}
	
	public int deleteNews(NewsRequest newsRequest, Integer adminId){
		int res=0;
		res = newsMapper.deleteByPrimaryKey(newsRequest.getNewsId());
		return res;		
	}
	
	public List<NewsView> queryNewsViewList(NewsRequest newsRequest){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if(newsRequest.getLang()!=null )
			paramMap.put("lang",newsRequest.getLang());		
			
		List<GroupMonth> groupMonthList= newsMapper.queryNewsGroupList(paramMap);
		
		List<NewsView> newsViewList = new ArrayList<NewsView>();
		for(int i = 0 ; i< groupMonthList.size() ; i++)
		{
			NewsView newsView = new NewsView();
			GroupMonth groupMonth= groupMonthList.get(i);
			String year_month = String.valueOf(groupMonth.getGroupYear());
			int iMonth = groupMonth.getGroupMonth();
			switch (iMonth){
		    	case 1:
		    		year_month += "JAN";
		    		break;
		    	case 2:
		    		year_month += "FEB";
		    		break;
		    	case 3:
		    		year_month += "MAR";
		    		break;
		    	case 4:
		    		year_month += "APR";
		    		break;
		    	case 5:
		    		year_month += "MAY";
		    		break;
		    	case 6:
		    		year_month += "JUN";
		    		break;
		    	case 7:
		    		year_month += "JUL";
		    		break;
		    	case 8:
		    		year_month += "AUG";
		    		break;
		    	case 9:
		    		year_month += "SEP";
		    		break;
		    	case 10:
		    		year_month += "OCT";
		    		break;
		    	case 11:
		    		year_month += "NOV";
		    		break;
		    	case 12:
		    		year_month += "DEC";
		    		break;
			}    
	
			paramMap.clear();
			paramMap.put("year", groupMonth.getGroupYear());
			paramMap.put("month", groupMonth.getGroupMonth());
			paramMap.put("lang",newsRequest.getLang());
			
			List<News> newsList = newsMapper.queryNewsMonthList(paramMap);
			
			newsView.setMonth(year_month);
			newsView.setNewsList(newsList);
			newsViewList.add(newsView);
		}
		return newsViewList;
	}
	
	public List<OperatingLogView> queryLogList(LogRequest logRequest){		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if(logRequest.getStartDate()!=null && !logRequest.getStartDate().equals(""))
			paramMap.put("startDate",logRequest.getStartDate());
		if(logRequest.getEndDate()!=null && !logRequest.getEndDate().equals(""))
			paramMap.put("endDate",logRequest.getEndDate());	
		if(logRequest.getOperatingLogType()!=null )
			paramMap.put("operatingLogType",logRequest.getOperatingLogType());	
		if(logRequest.getTableId()!=null )
			paramMap.put("tableId",logRequest.getTableId());	
		if(logRequest.getOperatingMode()!=null )
			paramMap.put("operatingMode",logRequest.getOperatingMode());
		
		List<OperatingLogView> logList = operatingLogMapper.queryLogList(paramMap);
		return logList;
	}
	
	
}