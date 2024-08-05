package com.spm.resqjeevanredis.service;

import com.spm.resqjeevanredis.dto.Login;
import com.spm.resqjeevanredis.repository.*;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationProvider authenticationProvider;
    private final AdminInfoRepo adminInfoRepo;
    private final PersonnelInfoRepo personnelInfoRepo;
    private final ResourceDepotRepo resourceDepotRepo;

    public AuthenticationServiceImpl(AuthenticationProvider authenticationProvider, AdminInfoRepo adminInfoRepo, PersonnelInfoRepo personnelInfoRepo, ResourceDepotRepo resourceDepotRepo) {
        this.authenticationProvider = authenticationProvider;
        this.adminInfoRepo = adminInfoRepo;
        this.personnelInfoRepo = personnelInfoRepo;
        this.resourceDepotRepo = resourceDepotRepo;
    }

    @Override
    public UserDetails authenticate(Login login) {
        authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(),login.getPassword()));
        try
        {
            return adminInfoRepo.findById(login.getUsername()).orElseThrow(()-> new UsernameNotFoundException("Invalid Username or Password"));
        }catch (UsernameNotFoundException exception){
            try{
                return personnelInfoRepo.findById(login.getUsername()).orElseThrow(()-> new UsernameNotFoundException("Invalid Username or Password"));
            }catch (UsernameNotFoundException exception1){
                return resourceDepotRepo.findById(login.getUsername()).orElseThrow(()-> new UsernameNotFoundException("Invalid Username or Password"));
            }

        }


    }
}
