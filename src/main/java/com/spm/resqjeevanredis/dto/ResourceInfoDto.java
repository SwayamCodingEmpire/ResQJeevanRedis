package com.spm.resqjeevanredis.dto;

import com.spm.resqjeevanredis.helper.ResourceType;
import lombok.*;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResourceInfoDto {
    private String resourceId;
    private String resourceName;
    private ResourceType resourceType;
    private long unitsAvailable;
    private String resourceDepotId;
}
