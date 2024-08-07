package com.spm.resqjeevanredis.service;

import com.spm.resqjeevanredis.dto.AdminInfoDto;
import com.spm.resqjeevanredis.entity.AdminInfo;
import com.spm.resqjeevanredis.exceptions.UsernameAlreadyExistsException;
import com.spm.resqjeevanredis.helper.AppConstants;
import com.spm.resqjeevanredis.helper.Status;
import com.spm.resqjeevanredis.repository.AdminInfoDao;
import com.spm.resqjeevanredis.repository.AdminInfoRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminInfoServiceImpl implements AdminInfoService {
    private final MapperServiceImpl mapperService;
    private final AdminInfoRepo adminInfoRepo;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(AdminInfoServiceImpl.class);

    public AdminInfoServiceImpl(MapperServiceImpl mapperService, AdminInfoRepo adminInfoRepo, PasswordEncoder passwordEncoder) {
        this.mapperService = mapperService;
        this.adminInfoRepo = adminInfoRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AdminInfoDto saveAdmin(AdminInfoDto adminInfoDto, String role){
            AdminInfo adminInfo = mapperService.convertToAdminInfo(adminInfoDto);
            adminInfo.setPassword(passwordEncoder.encode(adminInfo.getPassword()));
            adminInfo.setRole(role);
            if(adminInfoRepo.existsById(adminInfo.getUsername())){
                throw new UsernameAlreadyExistsException("An Admin already exists with username : "+adminInfo.getUsername());
            }
            else{
                return mapperService.convertToAdminInfoDto(adminInfoRepo.save(adminInfo));
            }
    }

    @CachePut(value = "AdminInfo", key = "#result.username")
    public AdminInfo makeControlRoomOnline(){
         AdminInfo controlRoom = (AdminInfo) SecurityContextHolder.getContext().getAuthentication();
         controlRoom.setStatus(Status.ONLINE);
         return adminInfoRepo.save(controlRoom);
    }

    @Override
    @CacheEvict(value = "AdminInfo", key = "#result.username")
    public AdminInfo makeControlRoomOffline() {
        AdminInfo controlRoom = (AdminInfo) SecurityContextHolder.getContext().getAuthentication();
        controlRoom.setStatus(Status.OFFLINE);
        return adminInfoRepo.save(controlRoom);
    }
}
