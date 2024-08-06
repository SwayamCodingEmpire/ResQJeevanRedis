package com.spm.resqjeevanredis.controller;

import com.spm.resqjeevanredis.dto.FetchRequest;
import com.spm.resqjeevanredis.dto.RequesterDto;
import com.spm.resqjeevanredis.service.ControlRoomServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Get Allocation Recommendation", description = "Get Allocation Recommendation only in from of HashMap")
    public ResponseEntity<HashMap<String,Long>> getAllocationRecommendation(@RequestBody RequesterDto requesterDto){
        return ResponseEntity.ok(controlRoomService.distributeRequestedResources(requesterDto));
    }


    @PutMapping("/fetch-and-send-to-depot")
    @Operation(summary = "Fetch Request And Send To Depot", description = "This is a http request response, which when called, sends the appropriate requests to resource Depots via Redis Pub-Sub and then Web Socket")
    public ResponseEntity<String> requestFetchAndSendToDepot(FetchRequest allocations){
        redisTemplate.convertAndSend(requestToDepotChannelTopic.getTopic(),allocations);
        return ResponseEntity.ok("Request Sent Successfully");
    }
}
