package com.spm.resqjeevanredis.repository;

import com.spm.resqjeevanredis.entity.AdminInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AdminInfoDao {
//    private final RedisTemplate redisTemplate;
//    public static final String HASH_KEY ="AdminInfo";
//    private final Logger logger = LoggerFactory.getLogger(AdminInfoDao.class);
//
//    public AdminInfoDao(RedisTemplate redisTemplate) {
//        this.redisTemplate = redisTemplate;
//    }
//
//    public Boolean save(AdminInfo adminInfo){
//        return redisTemplate.opsForHash().putIfAbsent(HASH_KEY,adminInfo.getUsername(),adminInfo);
//    }
//
//    public Optional<AdminInfo> findById(String username){
//        logger.info("Fetching Data of Admin from Database with id : {}",username);
//        return Optional.ofNullable((AdminInfo) redisTemplate.opsForHash().get(HASH_KEY,username)) ;
//    }
//
//    public Boolean existsById(String username){
//        logger.info("Checking the existance of the user from database with id : {}",username);
//        return redisTemplate.opsForHash().hasKey(HASH_KEY,username);
//    }
}
