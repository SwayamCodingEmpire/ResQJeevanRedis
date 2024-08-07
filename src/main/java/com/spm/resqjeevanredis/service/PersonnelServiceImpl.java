package com.spm.resqjeevanredis.service;

import com.spm.resqjeevanredis.entity.PersonnelInfo;
import com.spm.resqjeevanredis.helper.Status;
import com.spm.resqjeevanredis.repository.PersonnelInfoRepo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.security.core.context.SecurityContextHolder;

public class PersonnelServiceImpl implements PersonnelService {
    private final PersonnelInfoRepo personnelInfoRepo;;

    public PersonnelServiceImpl(PersonnelInfoRepo personnelInfoRepo) {
        this.personnelInfoRepo = personnelInfoRepo;
    }

    @Override
    @CachePut(value = "PersonnelInfo", key = "#result.username")
    public PersonnelInfo makePersonnelOnline(){
         PersonnelInfo personnelInfo = (PersonnelInfo) SecurityContextHolder.getContext().getAuthentication();
         personnelInfo.setStatus(Status.ONLINE);
         return personnelInfoRepo.save(personnelInfo);
    }

    @Override
    @CacheEvict(value = "PersonnelInfo", key = "#result.username")
    public PersonnelInfo makePersonnelOffline() {
        PersonnelInfo personnelInfo = (PersonnelInfo) SecurityContextHolder.getContext().getAuthentication();
        personnelInfo.setStatus(Status.OFFLINE);
        return personnelInfoRepo.save(personnelInfo);
    }

}
