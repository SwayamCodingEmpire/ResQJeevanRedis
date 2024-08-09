package com.spm.resqjeevanredis.service;

import com.spm.resqjeevanredis.dto.ResourceDepotDto;
import com.spm.resqjeevanredis.entity.ResourceDepot;

public interface ResourceDepotService {
    ResourceDepotDto saveResourceDepot(ResourceDepotDto resourceDepotDto);
    ResourceDepotDto updateResourceDepot(ResourceDepotDto resourceDepotDto);
    ResourceDepotDto getResourceDepotById(String resourceDepotId);
    Boolean existsById(String resourceDepotId);
    Boolean deleteById(String resourceDepotId);
    ResourceDepot makeResourceDepotOnline(String username);
    ResourceDepot makeResourceDepotOffline(String username);
}
