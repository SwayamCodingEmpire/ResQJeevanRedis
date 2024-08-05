package com.spm.resqjeevanredis.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PersonnelInfoDto {
    private String regimentNo;
    private String username;
    private String password;
    private String name;
    private String positionRank;
    private String location;
    private MultipartFile image;
}
