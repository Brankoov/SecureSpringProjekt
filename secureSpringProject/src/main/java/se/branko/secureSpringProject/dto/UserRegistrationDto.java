package se.branko.secureSpringProject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UserRegistrationDto {
    @NotBlank
    private String username;

    @Pattern(regexp = "^(?=.*[A-Z])(?=(?:.*\\d){2,})(?=(?:.*[!@#$%&*]){2,}).{8,}$",
            message = "Lösenordet måste ha minst 8 tecken, 1 stor bokstav, 2 siffror och 2 specialtecken")
    private String password;

    @NotBlank
    private String role;

    private boolean consentGiven;

    public @NotBlank String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank String username) {
        this.username = username;
    }

    public @Pattern(regexp = "^(?=.*[A-Z])(?=(?:.*\\d){2,})(?=(?:.*[!@#$%&*]){2,}).{8,}$",
            message = "Lösenordet måste ha minst 8 tecken, 1 stor bokstav, 2 siffror och 2 specialtecken") String getPassword() {
        return password;
    }

    public void setPassword(@Pattern(regexp = "^(?=.*[A-Z])(?=(?:.*\\d){2,})(?=(?:.*[!@#$%&*]){2,}).{8,}$",
            message = "Lösenordet måste ha minst 8 tecken, 1 stor bokstav, 2 siffror och 2 specialtecken") String password) {
        this.password = password;
    }

    public @NotBlank String getRole() {
        return role;
    }

    public void setRole(@NotBlank String role) {
        this.role = role;
    }

    public boolean isConsentGiven() {
        return consentGiven;
    }

    public void setConsentGiven(boolean consentGiven) {
        this.consentGiven = consentGiven;
    }
}

