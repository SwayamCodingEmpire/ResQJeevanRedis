package com.spm.resqjeevanredis.entity;

import com.spm.resqjeevanredis.helper.Status;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@RedisHash("PersonnelInfo")
public class PersonnelInfo implements UserDetails, Serializable {
    @Id
    private String regimentNo;
    private String username;
    private String password;
    private String name;
    private String positionRank;
    private String unitId;
    private String image_public_id;
    private String location;
    private String role;
    private Status status;
    private String controlRoomId;
    private boolean isAccountNonExpired=true;
    private boolean isAccountNonLocked=true;
    private boolean isCredentialsNonExpired=true;
    private boolean isEnabled=true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }
}
