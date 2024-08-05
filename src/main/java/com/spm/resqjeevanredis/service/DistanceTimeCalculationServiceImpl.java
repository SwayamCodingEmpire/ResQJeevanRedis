package com.spm.resqjeevanredis.service;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixRow;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Service
public class DistanceTimeCalculationServiceImpl implements DistanceTimeCalculationService {
    private final GeoApiContext geoApiContext;

    public DistanceTimeCalculationServiceImpl(GeoApiContext geoApiContext) {
        this.geoApiContext = geoApiContext;
    }

    @Override
    public List<Double> getTravelTimes(double[][] origins, double[] destination) {
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
        } catch (IOException | ApiException | InterruptedException e){
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
