package com.spm.resqjeevanredis.service;

import com.spm.resqjeevanredis.entity.AdminInfo;
import com.spm.resqjeevanredis.entity.PersonnelInfo;
import com.spm.resqjeevanredis.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service

public class CustomUserDetailService implements UserDetailsService {
    private final AdminInfoRepo adminInfoRepo;
    private final PersonnelInfoRepo personnelInfoRepo;
    private final ResourceDepotRepo resourceDepotRepo;
    private final Logger logger = LoggerFactory.getLogger(CustomUserDetailService.class);

    public CustomUserDetailService(AdminInfoRepo adminInfoRepo, PersonnelInfoRepo personnelInfoRepo, ResourceDepotRepo resourceDepotRepo) {
        this.adminInfoRepo = adminInfoRepo;
        this.personnelInfoRepo = personnelInfoRepo;
        this.resourceDepotRepo = resourceDepotRepo;
    }

    @Override
    @Cacheable(key = "#username", value = "UserDetails")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Fetching User data from Database with username : {}",username);
        AdminInfo adminInfo = adminInfoRepo.findById(username).orElse(null);
        if(adminInfo==null){
            PersonnelInfo personnelInfo = personnelInfoRepo.findById(username).orElse(null);
            if (personnelInfo==null){
                logger.info("Fetching Resource Depot data from Database with username as personnel is null : {}",username);
                return resourceDepotRepo.findById(username).orElseThrow(()->new UsernameNotFoundException("User not found with username: "+username));
            }
            logger.info("Returning PersonnelInfo");
            return personnelInfo;
        }
        logger.info("Returning AdminInfo");
        return adminInfo;
    }
}
