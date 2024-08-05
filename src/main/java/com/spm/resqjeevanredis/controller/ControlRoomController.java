package com.spm.resqjeevanredis.controller;

import com.spm.resqjeevanredis.dto.FetchRequest;
import com.spm.resqjeevanredis.dto.RequesterDto;
import com.spm.resqjeevanredis.service.ControlRoomServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/control-room")
@Tag(name = "ControlRoomController", description = "APIs for control Room")
public class ControlRoomController {
    private final Logger logger = LoggerFactory.getLogger(ControlRoomController.class);
    private final ControlRoomServiceImpl controlRoomService;
    private final RedisTemplate redisTemplate;
    private final ChannelTopic requestToDepotChannelTopic;
    private final Receiver receiver;

    public ControlRoomController(ControlRoomServiceImpl controlRoomService, RedisTemplate redisTemplate, ChannelTopic requestToDepotChannelTopic, Receiver receiver) {
        this.controlRoomService = controlRoomService;
        this.redisTemplate = redisTemplate;
        this.requestToDepotChannelTopic = requestToDepotChannelTopic;
        this.receiver = receiver;
    }

    @PutMapping("/getAllocation-Recommendation")
    public ResponseEntity<HashMap<String,Long>> getAllocationRecommendation(@RequestBody RequesterDto requesterDto){
        return ResponseEntity.ok(controlRoomService.distributeRequestedResources(requesterDto));
    }

    @PutMapping("/fetch-and-send-to-depot")
    public ResponseEntity<String> requestFetchAndSendToDepot(FetchRequest allocations){
        redisTemplate.convertAndSend(requestToDepotChannelTopic.getTopic(),allocations);
        return ResponseEntity.ok("Request Sent Successfully");
    }
}
