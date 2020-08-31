package com.sc.api_backend.model.response;

import java.util.List;

import com.sc.db.content.model.Content;
import com.sc.db.contentitem.model.ContentItem;

public class ContentView {
	
	Content contentTw;
	Content contentEn;
	List<ContentItem> contentItemTwList;
	List<ContentItem> contentItemEnList;
	
	public Content getContentTw() {
		return contentTw;
	}
	public void setContentTw(Content contentTw) {
		this.contentTw = contentTw;
	}
	public Content getContentEn() {
		return contentEn;
	}
	public void setContentEn(Content contentEn) {
		this.contentEn = contentEn;
	}
	public List<ContentItem> getContentItemTwList() {
		return contentItemTwList;
	}
	public void setContentItemTwList(List<ContentItem> contentItemTwList) {
		this.contentItemTwList = contentItemTwList;
	}
	public List<ContentItem> getContentItemEnList() {
		return contentItemEnList;
	}
	public void setContentItemEnList(List<ContentItem> contentItemEnList) {
		this.contentItemEnList = contentItemEnList;
	}
	
}
