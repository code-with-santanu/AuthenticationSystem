package com.santanu.jwtAuth.model;

import com.santanu.jwtAuth.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
@Data
public class RegisterUser {
    @NotEmpty(message = "Firstname can't be null or empty")
    private String firstName;

    @NotEmpty(message = "Lastname can't be null or empty")
    private String lastName;

    @NotEmpty(message = "Email can't be null or empty")
    @Email(message = "Enter a correct email address")
    private String email;

    @NotEmpty(message = "Password can't be null or empty")
    @Size(max = 15, message = "Password should be maximum 15 character long")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$" , message = "This violets the password pattern")
    private String password;

    private Role role;

    public RegisterUser() {
        // set default role for any user as USER
        this.role = Role.USER;
    }
}


// Explaination of above password pattern regex:
/*
    ^                 # start-of-string
    (?=.*[0-9])       # a digit must occur at least once
    (?=.*[a-z])       # a lower case letter must occur at least once
    (?=.*[A-Z])       # an upper case letter must occur at least once
    (?=.*[@#$%^&+=])  # a special character must occur at least once
    (?=\S+$)          # no whitespace allowed in the entire string
    .{8,}             # anything, at least eight places though
    $                 # end-of-string
 */