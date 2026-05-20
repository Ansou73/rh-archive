// dto/response/AuthResponse.java
package com.rharchive.dto.response;

import lombok.*;

@Data @AllArgsConstructor
public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private String nom;
    private String email;
    private String role;
}