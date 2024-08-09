package com.spm.resqjeevanredis.service;

import com.spm.resqjeevanredis.dto.ResourceInfoDto;
import com.spm.resqjeevanredis.dto.SendRequestToDepotDto;

import java.security.Principal;
import java.util.List;
import java.util.Set;

public interface ResourceInfoService {
    ResourceInfoDto saveResource(ResourceInfoDto resourceInfoDto);
    List<ResourceInfoDto> saveResources(List<ResourceInfoDto> resourceInfoDtos);
    ResourceInfoDto getResourceById(String resourceId);
    ResourceInfoDto getResourceByName(String resourceName);
    Set<ResourceInfoDto> getAllResources(Principal principal);

    Boolean sendResources(SendRequestToDepotDto sendRequestToDepotDto);

    ResourceInfoDto deleteEntireResource(String resourceName);

    Object addAResource(String resourceName,long addedUnits);

    Boolean removeResource(String resourceName,long removedUnits);
}
