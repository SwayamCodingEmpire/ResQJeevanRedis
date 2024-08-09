package com.spm.resqjeevanredis.controller;

import com.spm.resqjeevanredis.dto.ResourceInfoDto;
import com.spm.resqjeevanredis.dto.SendRequestToDepotDto;
import com.spm.resqjeevanredis.service.ResourceInfoServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/resource-depot")
@Tag(name = "ResourceDepotController", description = "APIs for ResourceDepots")
public class ResourceDepotController {
    private final Logger logger = LoggerFactory.getLogger(ResourceDepotController.class);
    private final ResourceInfoServiceImpl resourceInfoService;

    public ResourceDepotController(ResourceInfoServiceImpl resourceInfoService) {
        this.resourceInfoService = resourceInfoService;
    }

    @PostMapping("/save-resource")
    @Operation(summary = "Save Resource", description = "Save Resource")
    public ResourceInfoDto saveResource(@RequestBody ResourceInfoDto resourceInfoDto) {
        logger.info(resourceInfoDto.toString());
        return resourceInfoService.saveResource(resourceInfoDto);
    }

    @GetMapping("/get-all-resources")
    @Operation(summary = "Get All Resources", description = "Get All Resources for the logged in resource depot")
    public ResponseEntity<Set<ResourceInfoDto>> getAllResources(Principal principal) {
        logger.info("Fetching all resources");
        return ResponseEntity.ok(resourceInfoService.getAllResources(principal));
    }

    @PutMapping ("/approve-request")
    @Operation(summary = "Approve Request", description = "Approve Request")
    public ResponseEntity<Boolean> approveRequest(@RequestBody SendRequestToDepotDto sendRequestToDepotDto){
        logger.info(sendRequestToDepotDto.toString());
        return ResponseEntity.ok(resourceInfoService.sendResources(sendRequestToDepotDto));
    }

    @DeleteMapping("/delete-entire-resource/{resourceName}")
    @Operation(summary = "Delete Entire Resource", description = "Delete Entire Resource")
    public ResponseEntity<ResourceInfoDto> deleteEntireResource(@PathVariable String resourceName){
        logger.info("Deleting entire resource");
        return ResponseEntity.ok(resourceInfoService.deleteEntireResource(resourceName));
    }

    @PutMapping("/addAResource/{resourceName}/{unitsAdded}")
    @Operation(summary = "Add Resource units", description = "Add Resource units")
    public ResponseEntity<ResourceInfoDto> addAResource(@PathVariable String resourceName,@PathVariable long unitsAdded){
        logger.info("Adding a resource");
        return ResponseEntity.ok(resourceInfoService.addAResource(resourceName,unitsAdded));
    }

    @PutMapping("/remove-resource/{resourceName}/{unitsRemoved}")
    @Operation(summary = "Remove Resource units", description = "Remove units of a Resource")
    public ResponseEntity<Boolean> removeResource(@PathVariable String resourceName,@PathVariable long unitsRemoved){
        logger.info("Removing a resource");
        return ResponseEntity.ok(resourceInfoService.removeResource(resourceName,unitsRemoved));
    }
}
