package com.santanu.jwtAuth.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class CurrentUserInfo {

    private String fullName;

    private String userName;

    private String password;

    private String role;

}
