package com.spm.resqjeevanredis.entity;

import com.spm.resqjeevanredis.helper.Status;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@RedisHash("ResourceDepot")
public class ResourceDepot implements UserDetails, Serializable {
    @Id
    private String username;
    private String password;
    private String location;
    private double lat;
    private double lng;
    private String role;
    private Status status;
    private Double timeToReach;
    @Reference
    private Hashtable<String,ResourceInfo> resourceInfos;
    private boolean isAccountNonExpired=true;
    private boolean isAccountNonLocked=true;
    private boolean isCredentialsNonExpired=true;
    private boolean isEnabled=true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }
}
