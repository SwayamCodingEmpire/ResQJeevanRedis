package com.spm.resqjeevanredis.entity;

import com.spm.resqjeevanredis.helper.ResourceType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@RedisHash("ResourceInfo")
public class ResourceInfo implements Serializable
{
    @Id
    private String resourceId;
    @Indexed
    private String resourceName;
    private ResourceType resourceType;
    private long unitsAvailable;
    @Indexed
    private String resourceDepotId;
}
