package com.spm.resqjeevanredis.service;

import com.spm.resqjeevanredis.entity.PersonnelInfo;

public interface PersonnelService {
    PersonnelInfo makePersonnelOnline(String username);
    PersonnelInfo makePersonnelOffline(String username);
}
