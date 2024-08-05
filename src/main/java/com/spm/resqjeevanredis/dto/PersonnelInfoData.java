package com.spm.resqjeevanredis.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PersonnelInfoData {
    private String regimentNo;
    private String username;
    private String password;
    private String name;
    private String positionRank;
    private String location;
}
