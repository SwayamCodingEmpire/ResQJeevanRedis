package com.spm.resqjeevanredis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class AdminInfoDto {
    @Schema(description = "Admin Name", example = "Admin1")
    private String adminName;
    private String username;
    private String password;
    private String location;
    private String role;
}
