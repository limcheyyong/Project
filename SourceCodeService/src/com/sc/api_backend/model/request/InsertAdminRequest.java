package com.sc.api_backend.model.request;

import com.sc.db.admininfo.model.AdminInfo;

public class InsertAdminRequest extends AdminInfo{
	private Integer createAdminId;

	public Integer getCreateAdminId() {
		return createAdminId;
	}

	public void setCreateAdminId(Integer createAdminId) {
		this.createAdminId = createAdminId;
	}

}
