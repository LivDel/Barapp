package com.foreach.barapp.dto.request;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String identifiant;
    private String motDePasse;
}
