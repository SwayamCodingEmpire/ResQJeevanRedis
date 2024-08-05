package com.spm.resqjeevanredis.controller;

import com.spm.resqjeevanredis.dto.RequesterDto;
import com.spm.resqjeevanredis.dto.TestingDto;
import com.spm.resqjeevanredis.service.ControlRoomServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/control-room")
public class ControlRoomController {
    private final ControlRoomServiceImpl controlRoomService;

    public ControlRoomController(ControlRoomServiceImpl controlRoomService) {
        this.controlRoomService = controlRoomService;
    }

    @PostMapping("/testing")
    public ResponseEntity<HashMap<String,Long>> testApi(@RequestBody RequesterDto requesterDto){
        return ResponseEntity.ok(controlRoomService.distributeRequestedResources(requesterDto));
    }
}
