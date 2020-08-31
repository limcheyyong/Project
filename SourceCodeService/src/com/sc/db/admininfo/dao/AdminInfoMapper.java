package com.sc.db.admininfo.dao;

import java.util.List;

import com.sc.db.admininfo.model.AdminInfo;
import com.sc.api_backend.model.request.LoginBackendRequest;
import com.sc.api_backend.model.request.UpdataPwRequest;

public interface AdminInfoMapper {
    int deleteByPrimaryKey(Integer adminId);

    int insert(AdminInfo record);

    int insertSelective(AdminInfo record);

    AdminInfo selectByPrimaryKey(Integer adminId);

    int updateByPrimaryKeySelective(AdminInfo record);

    int updateByPrimaryKey(AdminInfo record);
    // new add
    AdminInfo inspectPwd(LoginBackendRequest loginBackendReques);

    AdminInfo selectByUserCode(String userCode);

    List<AdminInfo> queryAdminList();

    AdminInfo inspectPwdForupdataPassWord(UpdataPwRequest updataPwRequest);
}