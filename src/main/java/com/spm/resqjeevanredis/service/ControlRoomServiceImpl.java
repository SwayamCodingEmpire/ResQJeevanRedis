package com.spm.resqjeevanredis.service;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixRow;
import com.spm.resqjeevanredis.dto.RequesterDto;
import com.spm.resqjeevanredis.entity.ResourceDepot;
import com.spm.resqjeevanredis.entity.ResourceInfo;
import com.spm.resqjeevanredis.helper.ResourceType;
import com.spm.resqjeevanredis.repository.ResourceDepotRepo;
import com.spm.resqjeevanredis.repository.ResourceInfoRepo;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ControlRoomServiceImpl implements ControlRoomService {
    private final ResourceDepotRepo resourceDepotRepo;
    private final ResourceInfoRepo resourceInfoRepo;
    private final GeoApiContext geoApiContext;

    public ControlRoomServiceImpl(ResourceDepotRepo resourceDepotRepo, ResourceInfoRepo resourceInfoRepo, GeoApiContext geoApiContext) {
        this.resourceDepotRepo = resourceDepotRepo;
        this.resourceInfoRepo = resourceInfoRepo;
        this.geoApiContext = geoApiContext;
    }
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
//            double cummulativeError = 0;
//            resourceDepotList.sort(Comparator.comparing(resourceDepot -> resourceDepot.getResourceInfos().get(requesterDto.getResourceName()).getUnitsAvailable()));
//            for(int i=0;i<resourceDepotList.size();i++){
//                amountToBeSent = (long) (resourceDepotList.get(i).getResourceInfos().get(requesterDto.getResourceName()).getUnitsAvailable()*requiredToAvailableRatio);
//                cummulativeError = (resourceDepotList.get(i).getResourceInfos().get(requesterDto.getResourceName()).getUnitsAvailable()*requiredToAvailableRatio) - amountToBeSent;
//                amountRemainingToBeAllocated -= amountToBeSent;
//                if(i==resourceDepotList.size()-1){
//                    amountToBeSent += Math.round(cummulativeError);
//                } else if (cummulativeError>=1) {
//                }
//
//            }
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
//    @Override
    private HashMap<Boolean,List<ResourceDepot>> getResourceDepotsForRequest(RequesterDto requesterDto) {
        Set<ResourceInfo> resourceInfos = resourceInfoRepo.findAllByResourceName(requesterDto.getResourceName());
        double[][] origins = new double[resourceInfos.size()][2];
        List<ResourceDepot> resourceDepots = null;
        List<ResourceDepot> resourceDepotList = new ArrayList<>();
        HashMap<Boolean,List<ResourceDepot>> result = new HashMap<>();
        int i=0;
        AtomicLong currAmount = new AtomicLong();
        int j;
        for (ResourceInfo resourceInfo : resourceInfos) {
            ResourceDepot resourceDepot = resourceDepotRepo.findById(resourceInfo.getResourceId()).orElseThrow(()->new UsernameNotFoundException("Resource Depot not found"));
            origins[i][0] = resourceDepot.getLat();
            origins[i][1] = resourceDepot.getLng();
            resourceDepots.add(resourceDepot);
            i++;
        }
        List<Double> travelTimes = getTravelTimes(origins,new double[]{requesterDto.getLat(),requesterDto.getLng()});
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


    private List<Double> getTravelTimes(double[][] origins,double[] destination){
        try{
            String[] originsString = convertCoordinates(origins);
            String destinationString = convertCoordinate(destination);
            DistanceMatrix result = DistanceMatrixApi.newRequest(geoApiContext)
                    .origins(originsString)
                    .destinations(destinationString)
                    .await();
            List<Double> travelTimes = new ArrayList<>();
            for(DistanceMatrixRow row : result.rows){
                for(DistanceMatrixElement element : row.elements){
                    if(element.duration!=null){
                        double minutes = element.duration.inSeconds/60.0;
                        travelTimes.add(minutes);
                    }
                    else{
                        travelTimes.add(Double.NaN);
                    }
                }
            }
            return travelTimes;
        } catch (IOException | ApiException  | InterruptedException e){
            e.printStackTrace();
            List<String> errorList = new ArrayList<>();
            errorList.add("Error occurred while fetching distance matrix.");
            throw new RuntimeException("Error occurred while fetching distance matrix.");
        }
    }


    private String[] convertCoordinates(double[][] coordinates) {
        String[] result = new String[coordinates.length];
        for(int i=0;i<coordinates.length;i++){
            result[i] = coordinates[i][0] + "," + coordinates[i][1];
        }
        return result;
    }
    private String convertCoordinate(double[] coordinates){
        return coordinates[0] + "," + coordinates[1];
    }


}
