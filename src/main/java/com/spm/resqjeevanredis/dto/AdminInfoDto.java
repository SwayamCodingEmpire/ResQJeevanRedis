package com.spm.resqjeevanredis.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class AdminInfoDto {
    private String adminName;
    private String username;
    private String password;
    private String location;
    private String role;
}
