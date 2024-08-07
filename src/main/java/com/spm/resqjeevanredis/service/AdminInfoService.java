package com.spm.resqjeevanredis.service;

import com.spm.resqjeevanredis.dto.AdminInfoDto;
import com.spm.resqjeevanredis.entity.AdminInfo;

public interface AdminInfoService {
    AdminInfoDto saveAdmin(AdminInfoDto adminInfoDto, String role) throws Exception;
    AdminInfo makeControlRoomOnline();
    AdminInfo makeControlRoomOffline();
}
