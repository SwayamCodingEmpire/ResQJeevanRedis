package com.spm.resqjeevanredis.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spm.resqjeevanredis.dto.LocationToBeSentToControlRoomDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver implements MessageListener {
    private LocationToBeSentToControlRoomDto locationToBeSentToControlRoomDto = null;
    private final Logger logger = LoggerFactory.getLogger(Receiver.class);

    @Override
    public void onMessage(Message message, byte[] pattern) {
        logger.info("Message received: {}", message.toString());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            logger.info("Message body: {}", message.getBody());
            locationToBeSentToControlRoomDto = objectMapper.readValue(message.getBody(), LocationToBeSentToControlRoomDto.class);
            logger.info("LocationToBeSentToControlRoomDto received: {}", locationToBeSentToControlRoomDto.toString());
        } catch (Exception e) {
            logger.error("Error while parsing message: {}", e.getMessage());
        }
    }

    public LocationToBeSentToControlRoomDto locationToBeSentToControlRoomDto(){
        return locationToBeSentToControlRoomDto;
    }
}
