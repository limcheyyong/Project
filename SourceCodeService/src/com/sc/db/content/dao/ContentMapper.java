package com.sc.db.content.dao;

import java.util.List;
import java.util.Map;

import com.sc.db.content.model.Content;

public interface ContentMapper {
    int deleteByPrimaryKey(Integer contentId);

    int insert(Content record);

    int insertSelective(Content record);

    Content selectByPrimaryKey(Integer contentId);

    int updateByPrimaryKeySelective(Content record);

    int updateByPrimaryKey(Content record);
    
    List<Content> queryContentList(Map<String, Object> paramMap);
    
}