package com.spm.resqjeevanredis.service;

import com.spm.resqjeevanredis.dto.*;
import com.spm.resqjeevanredis.entity.AdminInfo;
import com.spm.resqjeevanredis.entity.PersonnelInfo;
import com.spm.resqjeevanredis.entity.ResourceDepot;
import com.spm.resqjeevanredis.entity.ResourceInfo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class MapperServiceImpl implements MapperService {
    private final ModelMapper modelMapper;

    public MapperServiceImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public AdminInfo convertToAdminInfo(AdminInfoDto adminInfoDto) {
        return modelMapper.map(adminInfoDto, AdminInfo.class);
    }

    @Override
    public AdminInfoDto convertToAdminInfoDto(AdminInfo adminInfo) {
        return modelMapper.map(adminInfo,AdminInfoDto.class);
    }

    @Override
    public PersonnelInfo convertToPersonnelInfo(PersonnelInfoDto personnelInfoDto) {
        return modelMapper.map(personnelInfoDto, PersonnelInfo.class);
    }

    @Override
    public PersonnelInfoDto convertToPersonnelInfoDto(PersonnelInfo personnelInfo) {
        return modelMapper.map(personnelInfo, PersonnelInfoDto.class);
    }

    @Override
    public PersonnelInfoData convertToPersonnelInfoData(PersonnelInfo personnelInfo) {
        return modelMapper.map(personnelInfo, PersonnelInfoData.class);
    }

    @Override
    public ResourceDepot convertToResourceDepot(ResourceDepotDto resourceDepotDto) {
        return modelMapper.map(resourceDepotDto, ResourceDepot.class);
    }

    @Override
    public ResourceDepotDto convertToResourceDepotDto(ResourceDepot resourceDepot) {
        return modelMapper.map(resourceDepot, ResourceDepotDto.class);
    }

    @Override
    public ResourceInfo convertToResourceInfo(ResourceInfoDto resourceInfoDto) {
        return modelMapper.map(resourceInfoDto, ResourceInfo.class);
    }

    @Override
    public ResourceInfoDto convertToResourceInfoDto(ResourceInfo resourceInfo) {
        return modelMapper.map(resourceInfo, ResourceInfoDto.class);
    }
}
