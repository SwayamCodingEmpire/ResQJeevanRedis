package com.spm.resqjeevanredis.repository;

import com.spm.resqjeevanredis.entity.ResourceDepot;
import com.spm.resqjeevanredis.exceptions.UsernameAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public class ResourceDepotDao {
//    private final RedisTemplate redisTemplate;
//    private final String HASH_KEY = "ResourceDepot";
//    private final Logger logger = LoggerFactory.getLogger(ResourceDepotDao.class);
//
//    public ResourceDepotDao(RedisTemplate redisTemplate) {
//        this.redisTemplate = redisTemplate;
//    }
//
//    public ResourceDepot save(ResourceDepot resourceDepot){
//        if (redisTemplate.opsForHash().putIfAbsent(HASH_KEY,resourceDepot.getUsername(),resourceDepot)){
//            logger.info("Saving Resource Depot data to Database with username : {}",resourceDepot.getUsername());
//            return resourceDepot;
//        }
//        else {
//            throw new UsernameAlreadyExistsException("A Resource Depot already exists with username : "+resourceDepot.getUsername());
//        }
//    }
//
//    public Optional<ResourceDepot> findById(String username){
//        logger.info("Fetching data of Resource Depot from Database with username : {}",username);
//        return Optional.ofNullable((ResourceDepot) redisTemplate.opsForHash().get(HASH_KEY,username));
//    }
//
//    public ResourceDepot update(ResourceDepot resourceDepot){
//        logger.info("Updating Resource Depot data to Database with username : {}",resourceDepot.getUsername());
//        redisTemplate.opsForHash().put(HASH_KEY,resourceDepot.getUsername(),resourceDepot);
//        return resourceDepot;
//
//    }
//
//    public Boolean existsById(String username){
//        logger.info("Checking the existance of the Resource Depot from database with username : {}",username);
//        return redisTemplate.opsForHash().hasKey(HASH_KEY,username);
//    }
//
//    public Long deleteById(String username){
//        logger.info("Deleting the Resource Depot from database with username : {}",username);
//        return redisTemplate.opsForHash().delete(HASH_KEY,username);
//    }
}
