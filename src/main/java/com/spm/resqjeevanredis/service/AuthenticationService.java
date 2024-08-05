package com.spm.resqjeevanredis.service;

import com.spm.resqjeevanredis.dto.Login;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationService {
    UserDetails authenticate(Login login);
}
