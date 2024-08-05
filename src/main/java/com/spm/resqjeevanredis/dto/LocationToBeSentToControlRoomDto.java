package com.spm.resqjeevanredis.dto;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LocationToBeSentToControlRoomDto {
        private String senderId;
        private String recipientId;
        private double lat;
        private double lng;
    }
