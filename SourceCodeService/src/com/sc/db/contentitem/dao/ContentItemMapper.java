package com.sc.db.contentitem.dao;

import java.util.List;
import java.util.Map;

import com.sc.db.contentitem.model.ContentItem;

public interface ContentItemMapper {
    int deleteByPrimaryKey(Integer contentItemId);

    int insert(ContentItem record);

    int insertSelective(ContentItem record);

    ContentItem selectByPrimaryKey(Integer contentItemId);

    int updateByPrimaryKeySelective(ContentItem record);

    int updateByPrimaryKey(ContentItem record);
    
    List<ContentItem> queryContentItemList(Map<String, Object> paramMap);
    
    int deleteByContentId(Integer contentId);
    
}