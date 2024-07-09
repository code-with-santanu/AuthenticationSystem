package com.santanu.jwtAuth.model;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Data
public class AuthenticationRequest {

    @NonNull
    private String email;

    @NonNull
    private String password;
}
