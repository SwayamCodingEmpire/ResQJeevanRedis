package com.spm.resqjeevanredis.service;

import java.util.List;

public interface DistanceTimeCalculationService {
    List<Double> getTravelTimes(double[][] origins, double[] destination);
}
