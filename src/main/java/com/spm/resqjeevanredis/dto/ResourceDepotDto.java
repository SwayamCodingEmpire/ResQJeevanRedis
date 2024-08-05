package com.spm.resqjeevanredis.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResourceDepotDto {
    private String username;
    private String password;
    private String location;
    private double lat;
    private double lng;
}
