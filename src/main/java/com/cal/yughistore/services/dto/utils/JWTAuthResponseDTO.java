package com.cal.yughistore.services.dto.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class JWTAuthResponseDTO {
    private final String tokenType = "BEARER";
    private String accessToken;
}