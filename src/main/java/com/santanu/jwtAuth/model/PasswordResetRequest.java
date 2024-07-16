package com.santanu.jwtAuth.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class PasswordResetRequest {

    @NotEmpty(message = "Password can't be null or empty")
    @Size(max = 15, message = "Password should be maximum 15 character long")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$" , message = "This violets the password pattern")
    private String oldPassword;

    @NotEmpty(message = "Password can't be null or empty")
    @Size(max = 15, message = "Password should be maximum 15 character long")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$" , message = "This violets the password pattern")
    private String newPassword;
}
