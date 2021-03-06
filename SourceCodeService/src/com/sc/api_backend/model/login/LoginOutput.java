package com.sc.api_backend.model.login;

import com.sc.base.model.BaseVO;
import com.sc.db.admininfo.model.AdminInfo;

public class LoginOutput extends BaseVO{
	private Integer status;
	private String securityKey;
	private AdminInfo adminInfo;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getSecurityKey() {
		return securityKey;
	}

	public void setSecurityKey(String securityKey) {
		this.securityKey = securityKey;
	}

	public AdminInfo getAdminInfo() {
		return adminInfo;
	}

	public void setAdminInfo(AdminInfo adminInfo) {
		this.adminInfo = adminInfo;
	}

}
