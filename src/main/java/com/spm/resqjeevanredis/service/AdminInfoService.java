package com.spm.resqjeevanredis.service;

import com.spm.resqjeevanredis.dto.AdminInfoDto;

public interface AdminInfoService {
    AdminInfoDto saveAdmin(AdminInfoDto adminInfoDto, String role) throws Exception;
}
