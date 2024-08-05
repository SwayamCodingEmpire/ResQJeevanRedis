package com.spm.resqjeevanredis.controller;

import com.spm.resqjeevanredis.dto.AdminInfoDto;
import com.spm.resqjeevanredis.dto.ResourceDepotDto;
import com.spm.resqjeevanredis.helper.AppConstants;
import com.spm.resqjeevanredis.service.AdminInfoServiceImpl;
import com.spm.resqjeevanredis.service.ResourceDepotServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/super-admin")
public class SuperAdminController {
    private final Logger logger = LoggerFactory.getLogger(SuperAdminController.class);
    private final AdminInfoServiceImpl adminInfoService;
    private final ResourceDepotServiceImpl resourceDepotService;

    public SuperAdminController(AdminInfoServiceImpl adminInfoService, ResourceDepotServiceImpl resourceDepotService) {
        this.adminInfoService = adminInfoService;
        this.resourceDepotService = resourceDepotService;
    }

    @PostMapping("/register")
    public ResponseEntity<AdminInfoDto> registerSuperAdmin(@RequestBody AdminInfoDto adminInfoDto){
        logger.info(adminInfoDto.toString());
        return ResponseEntity.ok(adminInfoService.saveAdmin(adminInfoDto, AppConstants.ROLE_SUPER_ADMIN));
    }

    @PostMapping("/registrar-register")
    public ResponseEntity<AdminInfoDto> registerRegistrar(@RequestBody AdminInfoDto adminInfoDto){
        logger.info(adminInfoDto.toString());
        return ResponseEntity.ok(adminInfoService.saveAdmin(adminInfoDto, AppConstants.ROLE_REGISTRAR));
    }

    @PostMapping("/controlRoom-register")
    public ResponseEntity<AdminInfoDto> registerControlRoom(@RequestBody AdminInfoDto adminInfoDto){
        logger.info(adminInfoDto.toString());
        return ResponseEntity.ok(adminInfoService.saveAdmin(adminInfoDto, AppConstants.ROLE_CONTROL_ROOM));
    }

    @PostMapping("/resource-depot/register")
    public ResponseEntity<ResourceDepotDto> registerResourceDepot(@RequestBody ResourceDepotDto resourceDepotDto){
        logger.info(resourceDepotDto.toString());
        return ResponseEntity.ok(resourceDepotService.saveResourceDepot(resourceDepotDto));
    }

    @GetMapping("/resource-depot/{resourceDepotId}")
    public ResponseEntity<ResourceDepotDto> getResourceDepot(@PathVariable String resourceDepotId){
        logger.info(resourceDepotId);
        return ResponseEntity.ok(resourceDepotService.getResourceDepotById(resourceDepotId));
    }

    @DeleteMapping("/resource-depot/{resourceDepotId}")
    public ResponseEntity<Boolean> deleteResourceDepot(@PathVariable String resourceDepotId){
        logger.info(resourceDepotId);
        return ResponseEntity.ok(resourceDepotService.deleteById(resourceDepotId));
    }

}
