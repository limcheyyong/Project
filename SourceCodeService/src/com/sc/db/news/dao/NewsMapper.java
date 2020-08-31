package com.sc.db.news.dao;

import java.util.List;
import java.util.Map;

import com.sc.api_backend.model.response.GroupMonth;
import com.sc.db.news.model.News;

public interface NewsMapper {
    int deleteByPrimaryKey(Integer newsId);

    int insert(News record);

    int insertSelective(News record);

    News selectByPrimaryKey(Integer newsId);

    int updateByPrimaryKeySelective(News record);

    int updateByPrimaryKey(News record);
     
    List<News> queryNewsList(Map<String, Object> paramMap);
    
    List<GroupMonth> queryNewsGroupList(Map<String, Object> paramMap);
    
    List<News> queryNewsMonthList(Map<String, Object> paramMap);
}