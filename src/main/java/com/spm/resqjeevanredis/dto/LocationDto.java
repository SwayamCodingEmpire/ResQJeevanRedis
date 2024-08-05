package com.spm.resqjeevanredis.dto;

import lombok.*;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class LocationDto {
    private String recipientId;
    private double lat;
    private double lng;
    private Date timestamp;
}
