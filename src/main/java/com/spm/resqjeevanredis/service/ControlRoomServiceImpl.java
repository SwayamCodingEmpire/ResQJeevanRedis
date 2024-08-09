package com.spm.resqjeevanredis.service;

import com.spm.resqjeevanredis.dto.RequesterDto;
import com.spm.resqjeevanredis.entity.ResourceDepot;
import com.spm.resqjeevanredis.entity.ResourceInfo;
import com.spm.resqjeevanredis.exceptions.ResouceNotFoundException;
import com.spm.resqjeevanredis.helper.ResourceType;
import com.spm.resqjeevanredis.repository.ResourceDepotRepo;
import com.spm.resqjeevanredis.repository.ResourceInfoDao;
import com.spm.resqjeevanredis.repository.ResourceInfoRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ControlRoomServiceImpl implements ControlRoomService {
    private final ResourceDepotRepo resourceDepotRepo;
    private final ResourceInfoDao resourceInfoDao;
    private final DistanceTimeCalculationServiceImpl distanceTimeCalculationService;
    private final Logger logger = LoggerFactory.getLogger(ControlRoomServiceImpl.class);

    public ControlRoomServiceImpl(ResourceDepotRepo resourceDepotRepo, ResourceInfoDao resourceInfoDao, DistanceTimeCalculationServiceImpl distanceTimeCalculationService) {
        this.resourceDepotRepo = resourceDepotRepo;
        this.resourceInfoDao = resourceInfoDao;
        this.distanceTimeCalculationService = distanceTimeCalculationService;
    }
    @Override
    public HashMap<String,Long> distributeRequestedResources(RequesterDto requesterDto){
        HashMap<Boolean,List<ResourceDepot>> resourceDepotsForRequest = getResourceDepotsForRequest(requesterDto);
        HashMap<String,Long> result = new HashMap<>();
        long amountToBeSent;
        List<ResourceDepot> resourceDepotList = resourceDepotsForRequest.get(true);
        logger.info("Suceesfully inside distributeeRequestedResources");
        logger.info(resourceDepotList.toString());
        if(resourceDepotList!=null){

            long totalAmountAvailable = 0;
            long amountRemainingToBeAllocated = requesterDto.getAmount();
            for(int i=0;i<resourceDepotList.size();i++){
                logger.info("Inside first forloop distributeRequestedResources");
                totalAmountAvailable+= resourceInfoDao.findById(resourceDepotList.get(i).getResourceInfos().get(requesterDto.getResourceName())).orElseThrow(()->new ResouceNotFoundException("Resource not found")).getUnitsAvailable();
            }
            double requiredToAvailableRatio = (double) amountRemainingToBeAllocated /totalAmountAvailable;
            resourceDepotList.sort(Comparator.comparing(resourceDepot -> resourceInfoDao.findById(resourceDepot.getResourceInfos().get(requesterDto.getResourceName())).orElseThrow(()->new ResouceNotFoundException("Resource not found")).getUnitsAvailable()
            ));
            for(int i=0;i<resourceDepotList.size();i++){
                if(i==resourceDepotList.size()-1){
                    long difference = amountRemainingToBeAllocated - resourceInfoDao.findById(resourceDepotList.get(i).getResourceInfos().get(requesterDto.getResourceName())).orElseThrow(()->new ResouceNotFoundException("Resource Not Found")).getUnitsAvailable();
                        int j=i-1;
                        while(difference>0){
                            amountToBeSent = (long) (resourceInfoDao.findById(resourceDepotList.get(j).getResourceInfos().get(requesterDto.getResourceName())).orElseThrow(()->new ResouceNotFoundException("Resource Not Found")).getUnitsAvailable()*requiredToAvailableRatio)+1;
                            if(amountToBeSent>resourceInfoDao.findById(resourceDepotList.get(j).getResourceInfos().get(requesterDto.getResourceName())).orElseThrow(()->new ResouceNotFoundException("Resource Not Found")).getUnitsAvailable()){
                                j--;
                            }
                            else{
                                difference--;
                                j--;
                                result.put(resourceDepotList.get(j).getUsername(), amountToBeSent);
                            }
                        }
                        amountToBeSent = amountRemainingToBeAllocated;
                        result.put(resourceDepotList.get(i).getUsername(), amountToBeSent);
                        return result;

                }
                else if(i==resourceDepotList.size()-2){
                    amountToBeSent = (long) (resourceInfoDao.findById(resourceDepotList.get(i).getResourceInfos().get(requesterDto.getResourceName())).orElseThrow(()->new ResouceNotFoundException ("Resource Not Found")).getUnitsAvailable()*requiredToAvailableRatio) + 1;
                    if(amountToBeSent>resourceInfoDao.findById(resourceDepotList.get(i).getResourceInfos().get(requesterDto.getResourceName())).orElseThrow(()-> new ResouceNotFoundException("Resource Not Found")).getUnitsAvailable()){
                        amountToBeSent = resourceInfoDao.findById(resourceDepotList.get(i).getResourceInfos().get(requesterDto.getResourceName())).orElseThrow(()->new ResouceNotFoundException("Resource Not Found")).getUnitsAvailable();
                    }
                    result.put(resourceDepotList.get(i).getUsername(), amountToBeSent);
                }
                else{
                    amountToBeSent = (long) (resourceInfoDao.findById(resourceDepotList.get(i).getResourceInfos().get(requesterDto.getResourceName())).orElseThrow(()->new ResouceNotFoundException("Resource Not Found")).getUnitsAvailable()*requiredToAvailableRatio);
                    result.put(resourceDepotList.get(i).getUsername(), amountToBeSent);
                }
                amountRemainingToBeAllocated -= amountToBeSent;

            }

        }
        else {
            resourceDepotList=resourceDepotsForRequest.get(false);
            logger.info("Inside false;"+resourceDepotList.toString());
            long totalAmountAvailable = 0;
            long amountRemainingToBeAllocated = requesterDto.getAmount();
            for(int i=0;i<resourceDepotList.size();i++){
                totalAmountAvailable+=resourceInfoDao.findById(resourceDepotList.get(i).getResourceInfos().get(requesterDto.getResourceName())).orElseThrow(()->new ResouceNotFoundException("Resource Not Found")).getUnitsAvailable();
            }
            double requiredToAvailableRatio = (double) requesterDto.getAmount() /totalAmountAvailable;
            resourceDepotList.sort(Comparator.comparing(resourceDepot -> resourceInfoDao.findById(resourceDepot.getResourceInfos().get(requesterDto.getResourceName())).orElseThrow(()->new ResouceNotFoundException("Resource Not Found")).getUnitsAvailable()));
            for(int i=0;i<resourceDepotList.size();i++){
                if(i==resourceDepotList.size()-1){
                    result.put(resourceDepotList.get(i).getUsername(), (long) resourceInfoDao.findById(resourceDepotList.get(i).getResourceInfos().get(requesterDto.getResourceName())).orElseThrow(()->new ResouceNotFoundException("Resource Not Found")).getUnitsAvailable());
                }
                if(i==resourceDepotList.size()-2){
                    amountToBeSent = (long) (resourceInfoDao.findById(resourceDepotList.get(i).getResourceInfos().get(requesterDto.getResourceName())).orElseThrow(()->new ResouceNotFoundException("Resource Not Found")).getUnitsAvailable()*requiredToAvailableRatio) + 1;
                    result.put(resourceDepotList.get(i).getUsername(), amountToBeSent);
                }
                else{
                    amountToBeSent = (long) (resourceInfoDao.findById(resourceDepotList.get(i).getResourceInfos().get(requesterDto.getResourceName())).orElseThrow(()->new ResouceNotFoundException("Resource Not Found")).getUnitsAvailable()*requiredToAvailableRatio);
                    result.put(resourceDepotList.get(i).getUsername(), amountToBeSent);
                }
                amountRemainingToBeAllocated -= amountToBeSent;

            }


        }
        return result;
    }
    private HashMap<Boolean,List<ResourceDepot>> getResourceDepotsForRequest(RequesterDto requesterDto) {
        Set<ResourceInfo> resourceInfos = resourceInfoDao.findAllByResourceName(requesterDto.getResourceName());
        logger.info(resourceInfos.toString());
        double[][] origins = new double[resourceInfos.size()][2];
        List<ResourceDepot> resourceDepots = new ArrayList<>();
        List<ResourceDepot> resourceDepotList = new ArrayList<>();
        HashMap<Boolean,List<ResourceDepot>> result = new HashMap<>();
        int i=0;
        AtomicLong currAmount = new AtomicLong();
        int j;
        for (ResourceInfo resourceInfo : resourceInfos) {
            ResourceDepot resourceDepot = resourceDepotRepo.findById(resourceInfo.getResourceDepotId()).orElseThrow(()->new UsernameNotFoundException("Resource Depot not found"));
            logger.info(resourceDepot.toString());
            origins[i][0] = resourceDepot.getLat();
            origins[i][1] = resourceDepot.getLng();
            resourceDepots.add(resourceDepot);
            i++;
        }
        List<Double> travelTimes = distanceTimeCalculationService.getTravelTimes(origins,new double[]{requesterDto.getLat(),requesterDto.getLng()});
        logger.info("Travel Times"+travelTimes.toString());
//        for(ResourceDepot resourceDepot: resourceDepots){
//            logger.info("Travel TIms"+travelTimes.get(resourceDepots.indexOf(resourceDepot)).toString());
//            resourceDepot.setTimeToReach(travelTimes.get(resourceDepots.indexOf(resourceDepot)));
//            logger.info(resourceDepot.getTimeToReach().toString());
//        }

        for(i=0;i<resourceDepots.size();i++){
            if(travelTimes.get(i)==Double.NaN){
                resourceDepots.remove(i);
            }
            logger.info("Travel TIms"+travelTimes.get(i));
            resourceDepots.get(i).setTimeToReach(travelTimes.get(i));
            logger.info(resourceDepots.get(i).getTimeToReach().toString());
        }
        resourceDepots.sort(Comparator.comparing(ResourceDepot::getTimeToReach));
        for(j=0;j<resourceDepots.size();j++){
            resourceDepotList.add(resourceDepots.get(j));
            logger.info("Just Testing   "+resourceDepotList.toString());
            logger.info(resourceDepots.get(j).toString());
            if(resourceDepots.get(j).getResourceInfos().get(requesterDto.getResourceName())==null){
                logger.info("I got it . This is it");
            }
            logger.info("Lets check Requesyer Dto"+requesterDto.toString());
            logger.info("What the hell is happening" + resourceDepots.get(j).getResourceInfos().get(requesterDto.getResourceName()));
//            logger.info(String.valueOf(resourceInfoDao.findById(resourceDepots.get(j).getResourceInfos().get(requesterDto.getResourceName())).orElseThrow(()->new ResouceNotFoundException("Resource not found")).getUnitsAvailable()));
            currAmount.addAndGet(resourceInfoDao.findById(resourceDepots.get(j).getResourceInfos().get(requesterDto.getResourceName())).orElseThrow(()->new ResouceNotFoundException("Resource not found")).getUnitsAvailable());
            logger.info(currAmount.toString());
            logger.info("Inside main for loop Value of j is {}",j);
            if(currAmount.get() >=requesterDto.getAmount()){
                logger.info(String.valueOf(currAmount.get()));
                logger.info(String.valueOf(requesterDto.getAmount()));
                j++;
                break;
            }
            logger.info("Value of j is {}",j);
        }
        logger.info("After for loop , value of j is {}",j);
        logger.info("CurrAmount"+ String.valueOf(currAmount.get()));
        if(requesterDto.getResourceType().equals(ResourceType.EMERGENCY)){
            logger.info("Inside if block comapring resource type");
            result.put(true,resourceDepotList);
            return result;
        }
        else {
            logger.info("CurrAmount"+ String.valueOf(currAmount.get()));
            logger.info("Inside else block comparing resource type");
            logger.info("Value of j is {}",j);
            logger.info("Value of resourceDepots.size() is {}",resourceDepots.size());
            while(j<resourceDepots.size()){
                logger.info("outside while-else block comparing time to reach");
                logger.info("Value of j is {}",j);
                logger.info("Trying my best " + String.valueOf(resourceDepots.get(j).getTimeToReach()));
                logger.info("Wont give up" + String.valueOf(resourceDepots.get(0).getTimeToReach()));
                logger.info(String.valueOf(resourceDepots.get(j).getTimeToReach()-resourceDepots.get(0).getTimeToReach()));
                logger.info(resourceDepots.get(j).toString());
                if(resourceDepots.get(j).getTimeToReach()-resourceDepots.get(0).getTimeToReach() >=2880){
                    logger.info("Inside while-else block comparing time to reach");
                    logger.info("Value of j is {}",j);
                    logger.info(String.valueOf(resourceDepots.get(j).getTimeToReach()-resourceDepots.get(0).getTimeToReach()));
                    logger.info(resourceDepots.get(j).toString());
                    break;
                }
                else{
                    logger.info("Inside else-while-else block comparing time to reach");
                    logger.info(requesterDto.getResourceName());
                    logger.info(String.valueOf(currAmount.get()));
                    logger.info("resourceDepots.get(j)   " ,resourceDepots.get(j).toString());
                    logger.info("resourceDepots.get(j).getResourceInfos()  " ,resourceDepots.get(j).getResourceInfos().toString());
                    logger.info("I sill solv this error " + resourceDepots.get(j).getResourceInfos().get(requesterDto.getResourceName()));
                    if(requesterDto.getResourceName()==null){
                        logger.info("Why why why");
                    }
                    currAmount.addAndGet(resourceInfoDao.findById(resourceDepots.get(j).getResourceInfos().get(requesterDto.getResourceName())).orElseThrow(()->new ResouceNotFoundException("Resource not found")).getUnitsAvailable());
                    logger.info("Successfully added units available");
                    resourceDepotList.add(resourceDepots.get(j));
                    j++;
                }
            }
        }
        if(currAmount.get()<requesterDto.getAmount()){
            logger.info(String.valueOf(currAmount.get()));
            logger.info(String.valueOf(requesterDto.getAmount()));
            logger.info("False + " + resourceDepotList.toString());
            result.put(false,resourceDepotList);
            return result;
        }
        else{
            logger.info(String.valueOf(currAmount.get()));
            logger.info(String.valueOf(requesterDto.getAmount()));
            logger.info("True + " + resourceDepotList.toString());
            result.put(true,resourceDepotList);
            return result;
        }
    }
}
