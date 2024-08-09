package com.spm.resqjeevanredis.service;

import com.spm.resqjeevanredis.entity.PersonnelInfo;
import com.spm.resqjeevanredis.helper.Status;
import com.spm.resqjeevanredis.repository.PersonnelInfoRepo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PersonnelServiceImpl implements PersonnelService {
    private final PersonnelInfoRepo personnelInfoRepo;;

    public PersonnelServiceImpl(PersonnelInfoRepo personnelInfoRepo) {
        this.personnelInfoRepo = personnelInfoRepo;
    }

    @Override
    @CachePut(value = "PersonnelInfo", key = "#username")
    public PersonnelInfo makePersonnelOnline(String username){
         PersonnelInfo personnelInfo = personnelInfoRepo.findById(username).orElseThrow(()->new UsernameNotFoundException("User not found with username: "+username));
         personnelInfo.setStatus(Status.ONLINE);
         return personnelInfoRepo.save(personnelInfo);
    }

    @Override
    @CacheEvict(value = "PersonnelInfo", key = "#username")
    public PersonnelInfo makePersonnelOffline(String username) {
        PersonnelInfo personnelInfo = personnelInfoRepo.findById(username).orElseThrow(()->new UsernameNotFoundException("User not found with username: "+username));
        personnelInfo.setStatus(Status.OFFLINE);
        return personnelInfoRepo.save(personnelInfo);
    }

}
