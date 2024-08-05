package com.spm.resqjeevanredis.controller;

import com.spm.resqjeevanredis.dto.LocationDto;
import com.spm.resqjeevanredis.dto.LocationToBeSentToControlRoomDto;
import com.spm.resqjeevanredis.exceptions.WebSocketRelatedException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@Tag(name = "LocationFetchController", description = "APIs for Location Fetching using STOMP Web Socket")
public class LocationFetchController {
    private final Logger logger = LoggerFactory.getLogger(LocationFetchController.class);
    private final ChannelTopic locationChannelTopic;
    private final RedisTemplate redisTemplate;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final Receiver receiver;

    public LocationFetchController(SimpMessagingTemplate simpMessagingTemplate, ChannelTopic locationChannelTopic, RedisTemplate redisTemplate, SimpMessagingTemplate simpMessagingTemplate1, Receiver receiver) {
        this.locationChannelTopic = locationChannelTopic;
        this.redisTemplate = redisTemplate;
        this.simpMessagingTemplate = simpMessagingTemplate1;
        this.receiver = receiver;
    }

    @MessageMapping("/location")
    public LocationDto processMessage(@Payload LocationDto locationDto, Principal principal) {
        logger.info("Location received: {}", locationDto.toString());
        if (principal instanceof Authentication) {
            Authentication authentication = (Authentication) principal;
            if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                logger.info("Authenticated user: " + userDetails.getUsername());
            } else {
                logger.info("Could not retrieve Authentication object");
            }
        } else {
            logger.info("Principal is not an instance of Authentication");
        }
        logger.info(locationDto.toString());
        try {
            redisTemplate.convertAndSend(locationChannelTopic.getTopic(), new LocationToBeSentToControlRoomDto(
                    principal.getName(),
                    locationDto.getRecipientId(),
                    locationDto.getLat(),
                    locationDto.getLng()
            ));
            LocationToBeSentToControlRoomDto location = receiver.locationToBeSentToControlRoomDto();
            return locationDto;
        } catch (Exception e) {
            e.printStackTrace();
            throw new WebSocketRelatedException("Failed to publish message");
        }
    }
}
