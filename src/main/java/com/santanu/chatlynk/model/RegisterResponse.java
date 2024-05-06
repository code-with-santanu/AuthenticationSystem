package com.santanu.chatlynk.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterResponse {
    private int statusCode;

    private String message;
}
