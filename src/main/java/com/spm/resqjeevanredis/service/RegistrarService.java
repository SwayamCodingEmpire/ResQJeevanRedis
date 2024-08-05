package com.spm.resqjeevanredis.service;

import com.spm.resqjeevanredis.dto.AdminInfoDto;
import com.spm.resqjeevanredis.dto.PersonnelInfoData;
import com.spm.resqjeevanredis.dto.PersonnelInfoDto;

import java.io.InputStream;
import java.util.List;

public interface RegistrarService {
    PersonnelInfoData registerPersonnel(PersonnelInfoDto personnelInfoDto);
    PersonnelInfoData updatePersonnel(PersonnelInfoDto personnelInfoDto);

    Boolean deletePersonnel(String regimentNo);

    PersonnelInfoData getPersonnel(String regimentNo);

    InputStream getPersonnelImage(String regimentNo);

    AdminInfoDto getAdmin(String adminName);

    List<PersonnelInfoDto> getAllPersonnel();

}
