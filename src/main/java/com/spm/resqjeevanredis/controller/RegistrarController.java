package com.spm.resqjeevanredis.controller;

import com.spm.resqjeevanredis.dto.PersonnelInfoData;
import com.spm.resqjeevanredis.dto.PersonnelInfoDto;
import com.spm.resqjeevanredis.service.RegistrarServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;

@RestController
@RequestMapping("/registrar")
public class RegistrarController {
    private final Logger logger = LoggerFactory.getLogger(RegistrarController.class);
    private final RegistrarServiceImpl registrarService;

    public RegistrarController(RegistrarServiceImpl registrarService) {
        this.registrarService = registrarService;
    }

    @PostMapping("/register-personnel")
    public ResponseEntity<PersonnelInfoData> registerPersonnel(@ModelAttribute PersonnelInfoDto personnelInfoDto){
        logger.info("Registering personnel: {}", personnelInfoDto.toString());
        return ResponseEntity.ok(registrarService.registerPersonnel(personnelInfoDto));
    }

    @PutMapping("/update-personnel")
    public ResponseEntity<PersonnelInfoData> updatePersonnel(@ModelAttribute PersonnelInfoDto personnelInfoDto){
        logger.info("Updating personnel: {}", personnelInfoDto.toString());
        return ResponseEntity.ok(registrarService.updatePersonnel(personnelInfoDto));
    }

    @DeleteMapping("/delete-personnel/{regimentNo}")
    public ResponseEntity<Boolean> deletePersonnel(@PathVariable("regimentNo") String regimentNo){
        logger.info("Deleting personnel with regiment number: {}", regimentNo);
        return ResponseEntity.ok(registrarService.deletePersonnel(regimentNo));
    }

    @GetMapping("/get-personnel/{regimentNo}")
    public ResponseEntity<PersonnelInfoData> getPersonnel(@PathVariable("regimentNo") String regimentNo){
        logger.info("Fetching personnel with regiment number: {}", regimentNo);
        return ResponseEntity.ok(registrarService.getPersonnel(regimentNo));
    }

    @GetMapping("/get-personnel-image/{regimentNo}")
    @ResponseBody
    public ResponseEntity<InputStreamResource> getImageDynamicType(@PathVariable("regimentNo") String regimentNo){
        InputStream in = registrarService.getPersonnelImage(regimentNo);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(new InputStreamResource(in));
    }
}
