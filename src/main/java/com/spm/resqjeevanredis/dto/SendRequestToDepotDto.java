package com.spm.resqjeevanredis.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SendRequestToDepotDto {
    private String requester;
    private double lat;
    private double lng;
    private String resourceName;
    private Long amount;
}
