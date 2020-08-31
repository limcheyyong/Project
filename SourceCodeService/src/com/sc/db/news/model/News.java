package com.sc.db.news.model;

import java.util.Date;

public class News {
    private Integer newsId;

    private Integer isOnApp;

    private Integer isOnAppEn;

    private String newsTitle;

    private String summary;

    private String linkUrl;

    private String newsTitleEn;

    private String summaryEn;

    private String linkUrlEn;

    private String newsImage;

    private String newsImage2;

    private String newsImage3;

    private String postTimeText;

    private Date postTime;

    private Date importDate;

    private String note1;

    private String note2;

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
        this.newsTitle = newsTitle == null ? null : newsTitle.trim();
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary == null ? null : summary.trim();
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl == null ? null : linkUrl.trim();
    }

    public String getNewsTitleEn() {
        return newsTitleEn;
    }

    public void setNewsTitleEn(String newsTitleEn) {
        this.newsTitleEn = newsTitleEn == null ? null : newsTitleEn.trim();
    }

    public String getSummaryEn() {
        return summaryEn;
    }

    public void setSummaryEn(String summaryEn) {
        this.summaryEn = summaryEn == null ? null : summaryEn.trim();
    }

    public String getLinkUrlEn() {
        return linkUrlEn;
    }

    public void setLinkUrlEn(String linkUrlEn) {
        this.linkUrlEn = linkUrlEn == null ? null : linkUrlEn.trim();
    }

    public String getNewsImage() {
        return newsImage;
    }

    public void setNewsImage(String newsImage) {
        this.newsImage = newsImage == null ? null : newsImage.trim();
    }

    public String getNewsImage2() {
        return newsImage2;
    }

    public void setNewsImage2(String newsImage2) {
        this.newsImage2 = newsImage2 == null ? null : newsImage2.trim();
    }

    public String getNewsImage3() {
        return newsImage3;
    }

    public void setNewsImage3(String newsImage3) {
        this.newsImage3 = newsImage3 == null ? null : newsImage3.trim();
    }

    public String getPostTimeText() {
        return postTimeText;
    }

    public void setPostTimeText(String postTimeText) {
        this.postTimeText = postTimeText == null ? null : postTimeText.trim();
    }

    public Date getPostTime() {
        return postTime;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public String getNote1() {
        return note1;
    }

    public void setNote1(String note1) {
        this.note1 = note1 == null ? null : note1.trim();
    }

    public String getNote2() {
        return note2;
    }

    public void setNote2(String note2) {
        this.note2 = note2 == null ? null : note2.trim();
    }
}