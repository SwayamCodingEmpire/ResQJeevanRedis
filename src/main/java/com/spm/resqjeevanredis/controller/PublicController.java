package com.spm.resqjeevanredis.controller;

import com.spm.resqjeevanredis.dto.Login;
import com.spm.resqjeevanredis.dto.LoginResponse;
import com.spm.resqjeevanredis.entity.AdminInfo;
import com.spm.resqjeevanredis.helper.AppConstants;
import com.spm.resqjeevanredis.service.AdminInfoServiceImpl;
import com.spm.resqjeevanredis.service.AuthenticationServiceImpl;
import com.spm.resqjeevanredis.service.JwtServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {
    private final AdminInfoServiceImpl adminInfoService;
    private final AuthenticationServiceImpl authenticationService;
    private final JwtServiceImpl jwtService;
    private final Logger logger = LoggerFactory.getLogger(PublicController.class);

    public PublicController(AdminInfoServiceImpl adminInfoService, AuthenticationServiceImpl authenticationService, JwtServiceImpl jwtService) {
        this.adminInfoService = adminInfoService;
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
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
}
