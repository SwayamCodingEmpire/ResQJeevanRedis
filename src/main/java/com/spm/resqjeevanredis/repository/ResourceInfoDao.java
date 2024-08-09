package com.spm.resqjeevanredis.repository;

import com.spm.resqjeevanredis.entity.ResourceInfo;
import com.spm.resqjeevanredis.exceptions.ResouceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Component
public class ResourceInfoDao {
    private final ResourceInfoRepo resourceInfoRepo;
    private final CacheManager cacheManager;
    private final Logger logger = LoggerFactory.getLogger(ResourceInfoDao.class);

    public ResourceInfoDao(ResourceInfoRepo resourceInfoRepo, CacheManager cacheManager) {
        this.resourceInfoRepo = resourceInfoRepo;
        this.cacheManager = cacheManager;
    }

    public Set<ResourceInfo> findAllByResourceName(String resourceName){
        Set<ResourceInfo> resourceInfos = resourceInfoRepo.findAllByResourceName(resourceName);
        Cache cache = cacheManager.getCache("ResourceInfos");
        if(cache!=null){
            resourceInfos.forEach(resourceInfo -> cache.put(resourceInfo.getResourceId(),resourceInfo));
        }
        return resourceInfos;
    }

    public Optional<ResourceInfo> findById(String resourceId){
        Cache cache = cacheManager.getCache("ResourceInfos");
        if(cache!=null){
            logger.info("Fetching ResourceInfo from Cache with resourceId : {}",resourceId);
            ResourceInfo resourceInfo = cache.get(resourceId,ResourceInfo.class);
            if(resourceInfo!=null){
                logger.info("ResourceInfo found in Cache with resourceId : {}",resourceId);
                logger.info(resourceInfo.toString());
                return Optional.of(resourceInfo);
            }
        }
        logger.info("Fetching ResourceInfo from Database with resourceId : {}",resourceId);
        Optional<ResourceInfo> resourceInfo = resourceInfoRepo.findById(resourceId);
        return resourceInfo;
    }

//    private final RedisTemplate redisTemplate;
//    private static final String HASH_KEY = "ResourceInfo";
//    private static final String RESOURCE_NAME_PREFIX = "resourceinfo:resourceName:";
//    private static final String RESOURCE_DEPOT_ID_PREFIX = "ResourceInfo:depotId:";
//    private static final String COMPOSITE_KEY_PREFIX = "resourceinfo:composite:";
//    private final Logger logger = LoggerFactory.getLogger(ResourceInfoDao.class);
//
//    public ResourceInfoDao(RedisTemplate redisTemplate) {
//        this.redisTemplate = redisTemplate;
//    }
//
//    public ResourceInfo save(ResourceInfo resourceInfo){
//        if (redisTemplate.opsForHash().putIfAbsent(HASH_KEY,resourceInfo.getResourceId(),resourceInfo)){
//            return resourceInfo;
//        }
//        else {
//            throw new ResourceAlreadyExistsException("Resource already exists with id : "+resourceInfo.getResourceId());
//        }
//    }
//
//    public Optional<ResourceInfo> findById(String resourceId){
//        return Optional.ofNullable((ResourceInfo) redisTemplate.opsForHash().get(HASH_KEY,resourceId));
//    }
//
//    public Boolean existsById(String resourceId){
//        return redisTemplate.opsForHash().hasKey(HASH_KEY,resourceId);
//    }
//
//    public List<ResourceInfo> findByResourceName(String resourceName){
//        List<ResourceInfo> resourceInfos = new ArrayList<>();
//        String keyPattern = RESOURCE_NAME_PREFIX+resourceName+"*";
//        Set keys = redisTemplate.keys(keyPattern);
//        if(keys!=null){
//            keys.forEach(key->{
//                ResourceInfo resourceInfo = (ResourceInfo) redisTemplate.opsForHash().get(HASH_KEY,key);
//                resourceInfos.add(resourceInfo);
//            });
//        }
//        return resourceInfos;
//    }
//
//    public Optional<ResourceInfo> findByResourceNameAndDepotId(String resourceName, String depotId){
//        String compositeKey = COMPOSITE_KEY_PREFIX + depotId + ":" + resourceName;
//        String resourceId = (String) redisTemplate.opsForValue().get(compositeKey);
//
//        if(resourceId!=null){
//            return findById(resourceId);
//        }
//        return Optional.empty();
//    }
//
//    public Optional<List<ResourceInfo>> findAllByResourceDepotId(String resourceDepotId){
//        List<ResourceInfo> resourceInfos = new ArrayList<>();
//        String keyPattern = RESOURCE_DEPOT_ID_PREFIX + resourceDepotId + "*";
//        Set keys = redisTemplate.keys(keyPattern);
//        logger.info("kEYS" + keys.toString());
//        if(keys!=null){
//            keys.forEach(key->{
//                ResourceInfo resourceInfo = (ResourceInfo) redisTemplate.opsForHash().get(HASH_KEY,key);
//                logger.info(resourceInfo.toString());
//                resourceInfos.add(resourceInfo);
//            });
//        }
//        return Optional.of(resourceInfos);
//    }
}
