package com.spm.resqjeevanredis.repository;

import com.spm.resqjeevanredis.entity.ResourceInfo;
import lombok.NonNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ResourceInfoRepo extends CrudRepository<ResourceInfo,String> {
    @Cacheable(key = "#resourceName", value = "ResourceInfos")
    Set<ResourceInfo> findAllByResourceName(String resourceName);
    Set<ResourceInfo> findAllByResourceDepotId(String resourceDepotId);
    ResourceInfo findByResourceNameAndResourceDepotId(String resourceName, String resourceDepotId);
}
