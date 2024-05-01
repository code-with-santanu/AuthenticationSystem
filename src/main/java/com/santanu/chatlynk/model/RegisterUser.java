package com.santanu.chatlynk.model;

import com.santanu.chatlynk.entity.Role;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.stereotype.Service;

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
