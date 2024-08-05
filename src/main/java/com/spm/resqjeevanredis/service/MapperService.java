package com.spm.resqjeevanredis.service;

import com.spm.resqjeevanredis.dto.*;
import com.spm.resqjeevanredis.entity.AdminInfo;
import com.spm.resqjeevanredis.entity.PersonnelInfo;
import com.spm.resqjeevanredis.entity.ResourceDepot;
import com.spm.resqjeevanredis.entity.ResourceInfo;

public interface MapperService {
    AdminInfo convertToAdminInfo(AdminInfoDto adminInfoDto);
    AdminInfoDto convertToAdminInfoDto(AdminInfo adminInfo);
    PersonnelInfo convertToPersonnelInfo(PersonnelInfoDto personnelInfoDto);
    PersonnelInfoDto convertToPersonnelInfoDto(PersonnelInfo personnelInfo);
    PersonnelInfoData convertToPersonnelInfoData(PersonnelInfo personnelInfo);
    ResourceDepot convertToResourceDepot(ResourceDepotDto resourceDepotDto);
    ResourceDepotDto convertToResourceDepotDto(ResourceDepot resourceDepot);
    ResourceInfo convertToResourceInfo(ResourceInfoDto resourceDepotDto);
    ResourceInfoDto convertToResourceInfoDto(ResourceInfo resourceInfo);
}
