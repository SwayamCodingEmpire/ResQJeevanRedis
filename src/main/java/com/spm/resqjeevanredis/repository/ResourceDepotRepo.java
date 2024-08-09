package com.spm.resqjeevanredis.repository;

import com.spm.resqjeevanredis.entity.ResourceDepot;
import lombok.NonNull;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceDepotRepo extends CrudRepository<ResourceDepot,String> {
    @Override
    @CachePut(key = "#result.username", value ="UserDetails")
    ResourceDepot save(@NonNull ResourceDepot resourceDepot);
}
