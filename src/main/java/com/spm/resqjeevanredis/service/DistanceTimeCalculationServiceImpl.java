package com.spm.resqjeevanredis.service;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Service
public class DistanceTimeCalculationServiceImpl implements DistanceTimeCalculationService {
    private final GeoApiContext geoApiContext;
    private final Logger logger = LoggerFactory.getLogger(DistanceTimeCalculationServiceImpl.class);

    public DistanceTimeCalculationServiceImpl(GeoApiContext geoApiContext) {
        this.geoApiContext = geoApiContext;
    }

    @Override
    public List<Double> getTravelTimes(double[][] origins, double[] destination) {
        try{
            logger.info("Origins : " + String.valueOf(origins[0][0]));
            logger.info("Destination : " + String.valueOf(destination[0]));
            String[] originsString = convertCoordinates(origins);
            String destinationString = convertCoordinate(destination);
            DistanceMatrix result = DistanceMatrixApi.newRequest(geoApiContext)
                    .origins(originsString)
                    .destinations(destinationString)
                    .await();
            logger.info("Result : " + result.toString());
            logger.info("Status : " + result.rows[0].elements[0].status);
            logger.info("Duration : " + result.rows[0].elements[0].duration);
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
            logger.info("Travel times: " + travelTimes);
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
