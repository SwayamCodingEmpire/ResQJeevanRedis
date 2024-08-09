package com.spm.resqjeevanredis.controller;

import com.spm.resqjeevanredis.dto.Login;
import com.spm.resqjeevanredis.dto.LoginResponse;
import com.spm.resqjeevanredis.dto.TestingDto;
import com.spm.resqjeevanredis.service.AdminInfoServiceImpl;
import com.spm.resqjeevanredis.service.AuthenticationServiceImpl;
import com.spm.resqjeevanredis.service.DistanceTimeCalculationServiceImpl;
import com.spm.resqjeevanredis.service.JwtServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public")
@Tag(name = "PublicController", description = "APIs open to all")
public class PublicController {
    private final AdminInfoServiceImpl adminInfoService;
    private final AuthenticationServiceImpl authenticationService;
    private final JwtServiceImpl jwtService;
    private final DistanceTimeCalculationServiceImpl distanceTimeCalculationService;
    private final Logger logger = LoggerFactory.getLogger(PublicController.class);

    public PublicController(AdminInfoServiceImpl adminInfoService, AuthenticationServiceImpl authenticationService, JwtServiceImpl jwtService, DistanceTimeCalculationServiceImpl distanceTimeCalculationService) {
        this.adminInfoService = adminInfoService;
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
        this.distanceTimeCalculationService = distanceTimeCalculationService;
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Login for all users")
    public ResponseEntity<LoginResponse> loginAdmin(@RequestBody Login login){
        UserDetails userDetails = authenticationService.authenticate(login);
        logger.info(userDetails.toString());
        if(userDetails instanceof UserDetails){
            UserDetails authenticatedAdmin = userDetails;
            String jwtToken = jwtService.generateToken(authenticatedAdmin);
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(jwtToken);
            loginResponse.setExpiresIn(jwtService.getExpirationTime());
            return ResponseEntity.ok(loginResponse);
        }
        else{
            throw new IllegalStateException("user is Invalid");
        }
    }

    @PostMapping("/testing")
    public ResponseEntity<List<Double>> testingMaps(@RequestBody TestingDto testingDto){
        return ResponseEntity.ok(distanceTimeCalculationService.getTravelTimes(testingDto.getOrigins(), testingDto.getDestination()));
    }
}
