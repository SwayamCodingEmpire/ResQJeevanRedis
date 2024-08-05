package com.spm.resqjeevanredis.service;

import com.spm.resqjeevanredis.dto.ResourceInfoDto;
import com.spm.resqjeevanredis.dto.SendRequestToDepotDto;
import com.spm.resqjeevanredis.entity.ResourceDepot;
import com.spm.resqjeevanredis.entity.ResourceInfo;
import com.spm.resqjeevanredis.exceptions.ResouceNotFoundException;
import com.spm.resqjeevanredis.exceptions.ResourceAlreadyExistsException;
import com.spm.resqjeevanredis.repository.ResourceDepotDao;
import com.spm.resqjeevanredis.repository.ResourceDepotRepo;
import com.spm.resqjeevanredis.repository.ResourceInfoDao;
import com.spm.resqjeevanredis.repository.ResourceInfoRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;

@Service
public class ResourceInfoServiceImpl implements ResourceInfoService {
    private final MapperServiceImpl mapperService;
    private final ResourceInfoRepo resourceInfoRepo;
    private final ResourceDepotRepo resourceDepotRepo;
    private final Logger logger = LoggerFactory.getLogger(ResourceInfoServiceImpl.class);

    public ResourceInfoServiceImpl(MapperServiceImpl mapperService, ResourceInfoRepo resourceInfoRepo, ResourceDepotRepo resourceDepotRepo) {
        this.mapperService = mapperService;
        this.resourceInfoRepo = resourceInfoRepo;
        this.resourceDepotRepo = resourceDepotRepo;
    }

    @Override
    public ResourceInfoDto saveResource(ResourceInfoDto resourceInfoDto) {
        try
        {
            ResourceDepot resourceDepot = (ResourceDepot) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            resourceInfoDto.setResourceDepotId(resourceDepot.getUsername());
            logger.info(resourceInfoDto.toString());
            if(resourceDepot.getResourceInfos()==null){
                HashMap<String,ResourceInfo> resourceInfos = new HashMap<>();
                resourceInfos.put(resourceInfoDto.getResourceName(),mapperService.convertToResourceInfo(resourceInfoDto));
                resourceDepot.setResourceInfos(resourceInfos);
            }
            else {
                if (resourceDepot.getResourceInfos().put(resourceInfoDto.getResourceName(),mapperService.convertToResourceInfo(resourceInfoDto))!=null){
                    logger.error("Resource with name : " + resourceInfoDto.getResourceName() + " already exists in this Resource Depot");
                    throw new ResourceAlreadyExistsException("Resource with name : " + resourceInfoDto.getResourceName() + " already exists in this Resource Depot");
                }
            }
            resourceDepotRepo.save(resourceDepot);
            return mapperService.convertToResourceInfoDto(resourceInfoRepo.save(mapperService.convertToResourceInfo(resourceInfoDto)));
        }catch (ClassCastException exception){
            throw new AccessDeniedException("You are not authorized to access this functionality of this resource");
        }
    }

    @Override
    public List<ResourceInfoDto> saveResources(List<ResourceInfoDto> resourceInfoDtos) {
        try
        {
            ResourceDepot resourceDepot = (ResourceDepot) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<ResourceInfo> resourceInfos = new ArrayList<>();
            resourceInfoDtos.forEach(resourceInfoDto -> {
                resourceInfos.add(mapperService.convertToResourceInfo(resourceInfoDto));
            });
            resourceInfos.forEach(resourceInfo -> {
                resourceInfo.setResourceDepotId(resourceDepot.getUsername());
                if(resourceDepot.getResourceInfos().put(resourceInfo.getResourceName(),resourceInfo)==null){
                    resourceInfoRepo.save(resourceInfo);
                    resourceDepotRepo.save(resourceDepot);
                }
                else{
                    logger.error("Resource with name : " + resourceInfo.getResourceName() + " already exists in this Resource Depot");
                    throw new ResourceAlreadyExistsException("Resource with name : " + resourceInfo.getResourceName() + " already exists in this Resource Depot");
                }

            });
            return resourceInfoDtos;
        }catch (ClassCastException exception){
            throw new AccessDeniedException("You are not authorized to access this functionality of this resource");
        }
    }

    @Override
    public ResourceInfoDto getResourceById(String resourceId) {
        try
        {
            ResourceDepot resourceDepot = (ResourceDepot) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            ResourceInfo resourceInfo = resourceInfoRepo.findById(resourceId).orElseThrow(() -> new ResouceNotFoundException("Resource Not Found with id : " + resourceId));
            if (resourceDepot.getResourceInfos().containsKey(resourceInfo)) {
                return mapperService.convertToResourceInfoDto(resourceInfo);
            } else {
                throw new AccessDeniedException("You are not authorized to access this functionality of this resource");
            }
        }catch (ClassCastException exception){
            throw new AccessDeniedException("You are not authorized to access this functionality of this resource");
        }
    }

    @Override
    public ResourceInfoDto getResourceByName(String resourceName) {
        try
        {
            ResourceDepot resourceDepot = (ResourceDepot) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(resourceDepot.getResourceInfos().get(resourceName)!=null){
                ResourceInfo resourceInfo = resourceInfoRepo.findByResourceNameAndResourceDepotId(resourceName,resourceDepot.getUsername());
                if (resourceInfo==null){
                    throw new ResouceNotFoundException("No such Resource Exists in this Resource Depot");
                }
                return mapperService.convertToResourceInfoDto(resourceInfo);
            }
            else{
                throw new ResouceNotFoundException("No such Resource Exists in this Resource Depot");
            }

        }catch (ClassCastException exception){
            throw new AccessDeniedException("You are not authorized to access this functionality of this resource");
        }
    }

    @Override
    public Set<ResourceInfoDto> getAllResources(Principal principal) {
        try
        {
            ResourceDepot resourceDepot = (ResourceDepot) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            logger.info(resourceDepot.toString());
            Set<ResourceInfo> resourceInfos = resourceInfoRepo.findAllByResourceDepotId(resourceDepot.getUsername());
            logger.info(resourceInfos.toString());
            if(!resourceInfos.isEmpty()){
                Set<ResourceInfoDto> resourceInfoDtos = new HashSet<>();
                resourceInfos.forEach(resourceInfo -> {
                    logger.info(resourceInfo.toString());
                    resourceInfoDtos.add(mapperService.convertToResourceInfoDto(resourceInfo));
                });
                return resourceInfoDtos;
            }
            else{
                throw new ResouceNotFoundException("No Resources Found in this Resource Depot with id" + resourceDepot.getUsername());
            }

        }catch (ClassCastException exception){
            throw new AccessDeniedException("You are not authorized to access this functionality of this resource");
        }
    }

    @Override
    public Boolean sendResources(SendRequestToDepotDto sendRequestToDepotDto) {
        ResourceInfoDto resourceInfoDto = getResourceByName(sendRequestToDepotDto.getResourceName());
        resourceInfoDto.setUnitsAvailable(resourceInfoDto.getUnitsAvailable()-sendRequestToDepotDto.getAmount());
        try
        {
            resourceInfoRepo.save(mapperService.convertToResourceInfo(resourceInfoDto));
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
