package com.av.apivisa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseTO {

    private String token;
    private String type = "Bearer";
    private String email;
    private String name;
    private String role;
    private Long expiresIn;
}