package com.spm.resqjeevanredis.repository;

import com.spm.resqjeevanredis.entity.PersonnelInfo;
import com.spm.resqjeevanredis.exceptions.UsernameAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public class PersonnelInfoDao {
//    private final RedisTemplate redisTemplate;
//    private static final String HASH_KEY = "PersonnelInfo";
//    private final Logger logger = LoggerFactory.getLogger(PersonnelInfoDao.class);
//
//    public PersonnelInfoDao(RedisTemplate redisTemplate) {
//        this.redisTemplate = redisTemplate;
//    }
//
//    public PersonnelInfo save(PersonnelInfo personnelInfo){
//        if(redisTemplate.opsForHash().putIfAbsent(HASH_KEY,personnelInfo.getRegimentNo(),personnelInfo)){
//            return personnelInfo;
//        }
//        else {
//            throw new UsernameAlreadyExistsException("A Personnel already exists with id : "+personnelInfo.getRegimentNo());
//        }
//    }
//
//    public Optional<PersonnelInfo> findById(String regimentNo){
//        logger.info("Fetching Data of Personnel from Database with id : {}",regimentNo);
//        return Optional.ofNullable((PersonnelInfo) redisTemplate.opsForHash().get(HASH_KEY,regimentNo)) ;
//    }
//
//    public Boolean existsById(String regimentNo){
//        logger.info("Checking the existance of the user from database with id : {}",regimentNo);
//        return redisTemplate.opsForHash().hasKey(HASH_KEY,regimentNo);
//    }
//
//    public PersonnelInfo reSave(PersonnelInfo personnelInfo){
//        redisTemplate.opsForHash().put(HASH_KEY,personnelInfo.getRegimentNo(),personnelInfo);
//        return personnelInfo;
//    }
//
//    public Long deleteById(String regimentNo){
//        return redisTemplate.opsForHash().delete(HASH_KEY,regimentNo);
//    }
}
