package com.findthebusiness.backend.dto.authentication;

public class RegisterRequestDto {

    private String name;
    private String email;
    private String password;
    private String confirmationPassword;
    private Boolean hasCheckedTerms;

    public RegisterRequestDto() {
    }

    public String getName() {
        return name;
    }

    public RegisterRequestDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public RegisterRequestDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public RegisterRequestDto setPassword(String password) {
        this.password = password;
        return this;
    }


    public String getConfirmationPassword() {
        return confirmationPassword;
    }

    public RegisterRequestDto setConfirmationPassword(String confirmationPassword) {
        this.confirmationPassword = confirmationPassword;
        return this;
    }

    public Boolean getHasCheckedTerms() {
        return hasCheckedTerms;
    }

    public RegisterRequestDto setHasCheckedTerms(Boolean hasCheckedTerms) {
        this.hasCheckedTerms = hasCheckedTerms;
        return this;
    }
}
