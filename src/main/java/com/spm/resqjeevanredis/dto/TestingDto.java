package com.spm.resqjeevanredis.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class TestingDto {
    double[][] origins;
    double[] destination;
}
