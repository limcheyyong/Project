package com.sc.db.contentitem.model;

public class ContentItem {
    private Integer contentItemId;

    private Integer contentId;

    private Integer itemType;

    private String itemText;

    private String subText;

    private Integer itemValue;

    private String itemNote;

    private Integer orderNo;

    public Integer getContentItemId() {
        return contentItemId;
    }

    public void setContentItemId(Integer contentItemId) {
        this.contentItemId = contentItemId;
    }

    public Integer getContentId() {
        return contentId;
    }

    public void setContentId(Integer contentId) {
        this.contentId = contentId;
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    public String getItemText() {
        return itemText;
    }

    public void setItemText(String itemText) {
        this.itemText = itemText == null ? null : itemText.trim();
    }

    public String getSubText() {
        return subText;
    }

    public void setSubText(String subText) {
        this.subText = subText == null ? null : subText.trim();
    }

    public Integer getItemValue() {
        return itemValue;
    }

    public void setItemValue(Integer itemValue) {
        this.itemValue = itemValue;
    }

    public String getItemNote() {
        return itemNote;
    }

    public void setItemNote(String itemNote) {
        this.itemNote = itemNote == null ? null : itemNote.trim();
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }
}