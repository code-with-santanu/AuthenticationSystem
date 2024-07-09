package com.santanu.auth.model;

import com.santanu.auth.entity.Role;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
@Data
public class RegisterUser {
    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    @NonNull
    private String email;

    @NonNull
    private String password;

    private Role role;

    public RegisterUser() {
        this.role = Role.USER;
    }
}
