package com.spm.resqjeevanredis.repository;

import com.spm.resqjeevanredis.entity.ResourceDepot;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceDepotRepo extends CrudRepository<ResourceDepot,String> {
}
