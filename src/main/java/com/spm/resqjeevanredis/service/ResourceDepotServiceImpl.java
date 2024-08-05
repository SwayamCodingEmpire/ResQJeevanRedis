package com.spm.resqjeevanredis.service;

import com.spm.resqjeevanredis.dto.ResourceDepotDto;
import com.spm.resqjeevanredis.entity.ResourceDepot;
import com.spm.resqjeevanredis.exceptions.ResouceNotFoundException;
import com.spm.resqjeevanredis.exceptions.UsernameAlreadyExistsException;
import com.spm.resqjeevanredis.helper.AppConstants;
import com.spm.resqjeevanredis.repository.ResourceDepotDao;
import com.spm.resqjeevanredis.repository.ResourceDepotRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ResourceDepotServiceImpl implements ResourceDepotService {
    private final ResourceDepotRepo resourceDepotRepo;
    private final MapperServiceImpl mapperService;
    private final PasswordEncoder passwordEncoder;

    public ResourceDepotServiceImpl(ResourceDepotRepo resourceDepotRepo, MapperServiceImpl mapperService, PasswordEncoder passwordEncoder) {
        this.resourceDepotRepo = resourceDepotRepo;
        this.mapperService = mapperService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResourceDepotDto saveResourceDepot(ResourceDepotDto resourceDepotDto) {
        ResourceDepot resourceDepot = mapperService.convertToResourceDepot(resourceDepotDto);
        resourceDepot.setRole(AppConstants.ROLE_RESOURCE_DEPOT);
        resourceDepot.setPassword(passwordEncoder.encode(resourceDepot.getPassword()));
        if(!resourceDepotRepo.existsById(resourceDepot.getUsername())){
            return mapperService.convertToResourceDepotDto(resourceDepotRepo.save(resourceDepot));
        }
        else {
            throw new UsernameAlreadyExistsException("Resource Depot with username : "+resourceDepot.getUsername()+" already exists");
        }
    }

    @Override
    public ResourceDepotDto updateResourceDepot(ResourceDepotDto resourceDepotDto) {
        return null;
    }

    @Override
    public ResourceDepotDto getResourceDepotById(String resourceDepotId) {
        return mapperService.convertToResourceDepotDto(resourceDepotRepo.findById(resourceDepotId).orElseThrow(()-> new ResouceNotFoundException("Resource Depot not found with id : "+resourceDepotId)));
    }

    @Override
    public Boolean existsById(String resourceDepotId) {
        return resourceDepotRepo.existsById(resourceDepotId);
    }

    @Override
    public Boolean deleteById(String resourceDepotId) {
        resourceDepotRepo.deleteById(resourceDepotId);
        return !resourceDepotRepo.existsById(resourceDepotId);
    }
}
