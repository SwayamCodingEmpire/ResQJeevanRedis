package com.spm.resqjeevanredis.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spm.resqjeevanredis.dto.FetchRequest;
import com.spm.resqjeevanredis.dto.LocationToBeSentToControlRoomDto;
import com.spm.resqjeevanredis.dto.SendRequestToDepotDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class Receiver implements MessageListener {
    private LocationToBeSentToControlRoomDto locationToBeSentToControlRoomDto = null;
    private FetchRequest allocations;
    private SimpMessagingTemplate simpMessagingTemplate;
    private final Logger logger = LoggerFactory.getLogger(Receiver.class);



    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel());
        logger.info("Message received: {}", message.toString());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            logger.info("Message body: {}", message.getBody());
            if("pubsub:location-channel".equals(channel)){
                locationToBeSentToControlRoomDto = objectMapper.readValue(message.getBody(), LocationToBeSentToControlRoomDto.class);
                logger.info("LocationToBeSentToControlRoomDto received: {}", locationToBeSentToControlRoomDto.toString());
                simpMessagingTemplate.convertAndSendToUser(locationToBeSentToControlRoomDto.getRecipientId(), "/queue/messages", locationToBeSentToControlRoomDto);
            }
            else if("pubsub:depot-channel".equals(channel)){
                allocations = objectMapper.readValue(message.getBody(), FetchRequest.class);
                logger.info("Allocations Recieved : {}",allocations.toString());
                String[] recipient = allocations.getResourceDepotIds();
                Long[] amounts = allocations.getAmounts();
                for(int i=0;i<allocations.getResourceDepotIds().length;i++){
                    simpMessagingTemplate.convertAndSendToUser(recipient[i],"/queue/resource",new SendRequestToDepotDto(
                            allocations.getRequester(),
                            allocations.getLat(),
                            allocations.getLng(),
                            allocations.getResourceName(),
                            amounts[i]
                    ));
                }
            }

        } catch (Exception e) {
            logger.error("Error while parsing message: {}", e.getMessage());
        }
    }

    public LocationToBeSentToControlRoomDto locationToBeSentToControlRoomDto(){
        return locationToBeSentToControlRoomDto;
    }

    public FetchRequest fetchAndSendToDepotDto(){
        return allocations;
    }
}
