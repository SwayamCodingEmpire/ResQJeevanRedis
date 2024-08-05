package com.spm.resqjeevanredis.controller;

import com.spm.resqjeevanredis.dto.ResourceInfoDto;
import com.spm.resqjeevanredis.dto.SendRequestToDepotDto;
import com.spm.resqjeevanredis.service.ResourceInfoServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/resource-depot")
public class ResourceDepotController {
    private final Logger logger = LoggerFactory.getLogger(ResourceDepotController.class);
    private final ResourceInfoServiceImpl resourceInfoService;

    public ResourceDepotController(ResourceInfoServiceImpl resourceInfoService) {
        this.resourceInfoService = resourceInfoService;
    }

    @PostMapping("/save-resource")
    public ResourceInfoDto saveResource(@RequestBody ResourceInfoDto resourceInfoDto) {
        logger.info(resourceInfoDto.toString());
        return resourceInfoService.saveResource(resourceInfoDto);
    }

    @GetMapping("/get-all-resources")
    public ResponseEntity<Set<ResourceInfoDto>> getAllResources(Principal principal) {
        logger.info("Fetching all resources");
        return ResponseEntity.ok(resourceInfoService.getAllResources(principal));
    }

    @PostMapping("/approve-request")
    public ResponseEntity<Boolean> approveRequest(@RequestBody SendRequestToDepotDto sendRequestToDepotDto){
        logger.info(sendRequestToDepotDto.toString());
        return ResponseEntity.ok(resourceInfoService.sendResources(sendRequestToDepotDto));
    }
}
