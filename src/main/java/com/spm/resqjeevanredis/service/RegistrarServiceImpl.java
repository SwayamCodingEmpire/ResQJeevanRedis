package com.spm.resqjeevanredis.service;

import com.spm.resqjeevanredis.dto.AdminInfoDto;
import com.spm.resqjeevanredis.dto.PersonnelInfoData;
import com.spm.resqjeevanredis.dto.PersonnelInfoDto;
import com.spm.resqjeevanredis.entity.PersonnelInfo;
import com.spm.resqjeevanredis.exceptions.UsernameAlreadyExistsException;
import com.spm.resqjeevanredis.helper.AppConstants;
import com.spm.resqjeevanredis.repository.PersonnelInfoDao;
import com.spm.resqjeevanredis.repository.PersonnelInfoRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
@Service
public class RegistrarServiceImpl implements RegistrarService {
    private final Logger logger = LoggerFactory.getLogger(RegistrarServiceImpl.class);
    private final MapperServiceImpl mapperService;
    private final PasswordEncoder passwordEncoder;
    private final CloudinaryImageServiceImpl cloudinaryImageService;
    private final PersonnelInfoRepo personnelInfoRepo;

    public RegistrarServiceImpl(MapperServiceImpl mapperService, PasswordEncoder passwordEncoder, CloudinaryImageServiceImpl cloudinaryImageService, PersonnelInfoRepo personnelInfoRepo) {
        this.mapperService = mapperService;
        this.passwordEncoder = passwordEncoder;
        this.cloudinaryImageService = cloudinaryImageService;
        this.personnelInfoRepo = personnelInfoRepo;
    }

    @Override
    public PersonnelInfoData registerPersonnel(PersonnelInfoDto personnelInfoDto) {
        PersonnelInfo personnelInfo = mapperService.convertToPersonnelInfo(personnelInfoDto);
        personnelInfo.setPassword(passwordEncoder.encode(personnelInfoDto.getPassword()));
        personnelInfo.setUsername(personnelInfoDto.getRegimentNo());
        personnelInfo.setRole(AppConstants.ROLE_PERSONNEL);
        Map imageData = cloudinaryImageService.upload(personnelInfoDto.getImage());
        personnelInfo.setImage_public_id(imageData.get("public_id").toString());
        if(personnelInfoRepo.existsById(personnelInfo.getRegimentNo())){
            throw new UsernameAlreadyExistsException("Personnel with regiment number: "+personnelInfo.getRegimentNo()+" already exists");
        }
        else {
            return mapperService.convertToPersonnelInfoData(personnelInfoRepo.save(personnelInfo));
        }
    }

    @Override
    public PersonnelInfoData updatePersonnel(PersonnelInfoDto personnelInfoDto) {
        PersonnelInfo personnelInfo = mapperService.convertToPersonnelInfo(personnelInfoDto);
        personnelInfo.setPassword(passwordEncoder.encode(personnelInfoDto.getPassword()));
        personnelInfo.setUsername(personnelInfoDto.getRegimentNo());
        personnelInfo.setRole(AppConstants.ROLE_PERSONNEL);
        Map imageData = cloudinaryImageService.upload(personnelInfoDto.getImage());
        personnelInfo.setImage_public_id(imageData.get("public_id").toString());
        return mapperService.convertToPersonnelInfoData(personnelInfoRepo.save(personnelInfo));
    }
    @Override
    public Boolean deletePersonnel(String regimentNo) {
        personnelInfoRepo.deleteById(regimentNo);
        return !personnelInfoRepo.existsById(regimentNo);
    }

    @Override
    public PersonnelInfoData getPersonnel(String regimentNo) {
        return personnelInfoRepo.findById(regimentNo).map(mapperService::convertToPersonnelInfoData).orElseThrow(()-> new UsernameNotFoundException("Personnel with regiment number: "+regimentNo+" not found"));
    }

    @Override
    public InputStream getPersonnelImage(String regimentNo)  {
        try
        {
             String public_id = personnelInfoRepo.findById(regimentNo).map(PersonnelInfo::getImage_public_id).orElseThrow(() -> new UsernameNotFoundException("Personnel with regiment number: " + regimentNo + " not found"));
             return cloudinaryImageService.getImageFile(public_id);
        }catch (IOException e){
            throw new RuntimeException("Error fetching image: "+e.getMessage());
        }
    }

    @Override
    public AdminInfoDto getAdmin(String adminName) {
        return null;
    }

    @Override
    public List<PersonnelInfoDto> getAllPersonnel() {
        return null;
    }

    public Boolean promoteToLeader(String regimentNo){
        PersonnelInfo personnelInfo = personnelInfoRepo.findById(regimentNo).orElseThrow(()-> new UsernameNotFoundException("Personnel with regiment number: "+regimentNo+" not found"));
        personnelInfo.setRole(AppConstants.ROLE_LEADER);
        return personnelInfoRepo.save(personnelInfo)!=null;
    }

    public Boolean demoteToPersonnel(String regimentNo){
        PersonnelInfo personnelInfo = personnelInfoRepo.findById(regimentNo).orElseThrow(()-> new UsernameNotFoundException("Personnel with regiment number: "+regimentNo+" not found"));
        personnelInfo.setRole(AppConstants.ROLE_PERSONNEL);
        return personnelInfoRepo.save(personnelInfo)!=null;
    }
}
