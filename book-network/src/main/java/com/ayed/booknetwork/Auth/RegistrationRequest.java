package com.ayed.booknetwork.Auth;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class RegistrationRequest {
    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email should not be empty")
    @NotNull(message = "Email is required")
    private  String email;
    @NotNull(message = "Password is required")
    @NotEmpty(message = "Password should not be empty")
    @Size(min = 5, message = "Password should be at least 5 characters")
    private  String password;

    @NotNull(message = "First name is required")
    @NotEmpty(message = "First name should not be empty")

    private  String firstName;
    @NotNull(message = "Last name is required")
    @NotEmpty(message = "Last name should not be empty")
    private String lastName;
    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;
}


