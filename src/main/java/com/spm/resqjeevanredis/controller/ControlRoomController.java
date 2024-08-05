package com.spm.resqjeevanredis.controller;

import com.spm.resqjeevanredis.dto.TestingDto;
import com.spm.resqjeevanredis.service.ControlRoomServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<List<Double>> testApi(@RequestBody TestingDto testingDto){
        return ResponseEntity.ok(controlRoomService.getTravelTimes(testingDto.getOrigins(),testingDto.getDestination()));
    }
}
