package com.ayed.booknetwork.Auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
public class AuthenticationRequest {
    @Email(message = "Email should be valid")
    @NotNull(message = "Email is required")
    @NotEmpty(message = "Email should not be empty")
    private String email;

    @NotNull(message = "Password is required")
    @NotEmpty(message = "Password should not be empty")
    @Size(min = 5, message = "Password should be at least 5 characters")
    private String password;


}
