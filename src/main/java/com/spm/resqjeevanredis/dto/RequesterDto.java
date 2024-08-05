package com.spm.resqjeevanredis.dto;

import com.spm.resqjeevanredis.helper.ResourceType;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class RequesterDto {
    private String username;
    private double lat;
    private double lng;
    private long amount;
    private ResourceType resourceType;
    private String resourceName;
}
