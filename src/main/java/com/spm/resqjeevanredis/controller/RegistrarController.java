package com.spm.resqjeevanredis.controller;

import com.spm.resqjeevanredis.dto.PersonnelInfoData;
import com.spm.resqjeevanredis.dto.PersonnelInfoDto;
import com.spm.resqjeevanredis.service.RegistrarServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;

@RestController
@RequestMapping("/registrar")
@Tag(name = "RegistrarController", description = "APIs for registrar to register other entities")
public class RegistrarController {
    private final Logger logger = LoggerFactory.getLogger(RegistrarController.class);
    private final RegistrarServiceImpl registrarService;

    public RegistrarController(RegistrarServiceImpl registrarService) {
        this.registrarService = registrarService;
    }

    @PostMapping("/register-personnel")
    @Operation(summary = "Register Personnel", description = "Register Personnel through form")
    public ResponseEntity<PersonnelInfoData> registerPersonnel(@ModelAttribute PersonnelInfoDto personnelInfoDto){
        logger.info("Registering personnel: {}", personnelInfoDto.toString());
        return ResponseEntity.ok(registrarService.registerPersonnel(personnelInfoDto));
    }

    @PutMapping("/update-personnel")
    @Operation(summary = "Update Personnel", description = "Update Personnel through form ")
    public ResponseEntity<PersonnelInfoData> updatePersonnel(@ModelAttribute PersonnelInfoDto personnelInfoDto){
        logger.info("Updating personnel: {}", personnelInfoDto.toString());
        return ResponseEntity.ok(registrarService.updatePersonnel(personnelInfoDto));
    }

    @DeleteMapping("/delete-personnel/{regimentNo}")
    @Operation(summary = "Delete Personnel", description = "Delete Personnel")
    public ResponseEntity<Boolean> deletePersonnel(@PathVariable("regimentNo") String regimentNo){
        logger.info("Deleting personnel with regiment number: {}", regimentNo);
        return ResponseEntity.ok(registrarService.deletePersonnel(regimentNo));
    }

    @GetMapping("/get-personnel/{regimentNo}")
    public ResponseEntity<PersonnelInfoData> getPersonnel(@PathVariable("regimentNo") String regimentNo){
        logger.info("Fetching personnel with regiment number: {}", regimentNo);
        return ResponseEntity.ok(registrarService.getPersonnel(regimentNo));
    }

    @Operation(summary = "Get Personnel Image", description = "Get Personnel Image")
    @GetMapping("/get-personnel-image/{regimentNo}")
    @ResponseBody
    public ResponseEntity<InputStreamResource> getImageDynamicType(@PathVariable("regimentNo") String regimentNo){
        InputStream in = registrarService.getPersonnelImage(regimentNo);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(new InputStreamResource(in));
    }
}
