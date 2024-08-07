package com.spm.resqjeevanredis.service;

import com.google.maps.GeoApiContext;
import com.spm.resqjeevanredis.dto.RequesterDto;
import com.spm.resqjeevanredis.entity.AdminInfo;
import com.spm.resqjeevanredis.entity.ResourceDepot;
import com.spm.resqjeevanredis.entity.ResourceInfo;
import com.spm.resqjeevanredis.helper.ResourceType;
import com.spm.resqjeevanredis.helper.Status;
import com.spm.resqjeevanredis.repository.ResourceDepotRepo;
import com.spm.resqjeevanredis.repository.ResourceInfoRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ControlRoomServiceImpl implements ControlRoomService {
    private final ResourceDepotRepo resourceDepotRepo;
    private final ResourceInfoRepo resourceInfoRepo;
    private final DistanceTimeCalculationServiceImpl distanceTimeCalculationService;
    private final Logger logger = LoggerFactory.getLogger(ControlRoomServiceImpl.class);

    public ControlRoomServiceImpl(ResourceDepotRepo resourceDepotRepo, ResourceInfoRepo resourceInfoRepo, DistanceTimeCalculationServiceImpl distanceTimeCalculationService) {
        this.resourceDepotRepo = resourceDepotRepo;
        this.resourceInfoRepo = resourceInfoRepo;
        this.distanceTimeCalculationService = distanceTimeCalculationService;
    }
    @Override
    public HashMap<String,Long> distributeRequestedResources(RequesterDto requesterDto){
        HashMap<Boolean,List<ResourceDepot>> resourceDepotsForRequest = getResourceDepotsForRequest(requesterDto);
        HashMap<String,Long> result = new HashMap<>();
        long amountToBeSent;
        List<ResourceDepot> resourceDepotList = resourceDepotsForRequest.get(true);
        if(resourceDepotList!=null){

            long totalAmountAvailable = 0;
            long amountRemainingToBeAllocated = requesterDto.getAmount();
            for(int i=0;i<resourceDepotList.size();i++){
                totalAmountAvailable+=resourceDepotList.get(i).getResourceInfos().get(requesterDto.getResourceName()).getUnitsAvailable();
            }
            double requiredToAvailableRatio = (double) amountRemainingToBeAllocated /totalAmountAvailable;
            resourceDepotList.sort(Comparator.comparing(resourceDepot -> resourceDepot.getResourceInfos().get(requesterDto.getResourceName()).getUnitsAvailable()));
            for(int i=0;i<resourceDepotList.size();i++){
                if(i==resourceDepotList.size()-1){
                    long difference = amountRemainingToBeAllocated - resourceDepotList.get(i).getResourceInfos().get(requesterDto.getResourceName()).getUnitsAvailable();
                        int j=i-1;
                        while(difference>0){
                            amountToBeSent = (long) (resourceDepotList.get(j).getResourceInfos().get(requesterDto.getResourceName()).getUnitsAvailable()*requiredToAvailableRatio)+1;
                            if(amountToBeSent>resourceDepotList.get(j).getResourceInfos().get(requesterDto.getResourceName()).getUnitsAvailable()){
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
                    amountToBeSent = (long) (resourceDepotList.get(i).getResourceInfos().get(requesterDto.getResourceName()).getUnitsAvailable()*requiredToAvailableRatio) + 1;
                    if(amountToBeSent>resourceDepotList.get(i).getResourceInfos().get(requesterDto.getResourceName()).getUnitsAvailable()){
                        amountToBeSent = resourceDepotList.get(i).getResourceInfos().get(requesterDto.getResourceName()).getUnitsAvailable();
                    }
                    result.put(resourceDepotList.get(i).getUsername(), amountToBeSent);
                }
                else{
                    amountToBeSent = (long) (resourceDepotList.get(i).getResourceInfos().get(requesterDto.getResourceName()).getUnitsAvailable()*requiredToAvailableRatio);
                    result.put(resourceDepotList.get(i).getUsername(), amountToBeSent);
                }
                amountRemainingToBeAllocated -= amountToBeSent;

            }

        }
        else {
            long totalAmountAvailable = 0;
            long amountRemainingToBeAllocated = requesterDto.getAmount();
            for(int i=0;i<resourceDepotList.size();i++){
                totalAmountAvailable+=resourceDepotList.get(i).getResourceInfos().get(requesterDto.getResourceName()).getUnitsAvailable();
            }
            double requiredToAvailableRatio = (double) requesterDto.getAmount() /totalAmountAvailable;
            resourceDepotList.sort(Comparator.comparing(resourceDepot -> resourceDepot.getResourceInfos().get(requesterDto.getResourceName()).getUnitsAvailable()));
            for(int i=0;i<resourceDepotList.size();i++){
                if(i==resourceDepotList.size()-1){
                    result.put(resourceDepotList.get(i).getUsername(), (long) resourceDepotList.get(i).getResourceInfos().get(requesterDto.getResourceName()).getUnitsAvailable());
                }
                if(i==resourceDepotList.size()-2){
                    amountToBeSent = (long) (resourceDepotList.get(i).getResourceInfos().get(requesterDto.getResourceName()).getUnitsAvailable()*requiredToAvailableRatio) + 1;
                    result.put(resourceDepotList.get(i).getUsername(), amountToBeSent);
                }
                else{
                    amountToBeSent = (long) (resourceDepotList.get(i).getResourceInfos().get(requesterDto.getResourceName()).getUnitsAvailable()*requiredToAvailableRatio);
                    result.put(resourceDepotList.get(i).getUsername(), amountToBeSent);
                }
                amountRemainingToBeAllocated -= amountToBeSent;

            }


        }
        return result;
    }
    private HashMap<Boolean,List<ResourceDepot>> getResourceDepotsForRequest(RequesterDto requesterDto) {
        Set<ResourceInfo> resourceInfos = resourceInfoRepo.findAllByResourceName(requesterDto.getResourceName());
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
        for(ResourceDepot resourceDepot: resourceDepots){
            resourceDepot.setTimeToReach(travelTimes.get(resourceDepots.indexOf(resourceDepot)));
        }
        resourceDepots.sort(Comparator.comparing(ResourceDepot::getTimeToReach));
        for(j=0;j<resourceDepots.size();j++){
            resourceDepotList.add(resourceDepots.get(j));
            currAmount.addAndGet(resourceDepots.get(j).getResourceInfos().get(requesterDto.getResourceName()).getUnitsAvailable());
            if(currAmount.get() >=requesterDto.getAmount())
                break;
        }
        if(requesterDto.getResourceType().equals(ResourceType.EMERGENCY)){
            result.put(true,resourceDepotList);
            return result;
        }
        else {
            while(j<resourceDepots.size()){
                if(resourceDepots.get(j).getTimeToReach()-resourceDepots.get(0).getTimeToReach() >=60)
                    break;
                else{
                    currAmount.addAndGet(resourceDepots.get(j).getResourceInfos().get(requesterDto.getResourceName()).getUnitsAvailable());
                    resourceDepotList.add(resourceDepots.get(j));
                    j++;
                }
            }
        }
        if(currAmount.get()<requesterDto.getAmount()){
            result.put(false,resourceDepotList);
            return result;
        }
        else{
            result.put(true,resourceDepotList);
            return result;
        }
    }
}
