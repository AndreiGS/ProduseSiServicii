package com.findthebusiness.backend.dto.users;

public class ChangeInfoRequestDto {
    private String newEmail;
    private String newName;

    public ChangeInfoRequestDto() {
    }

    public ChangeInfoRequestDto(String newEmail, String newName) {
        this.newEmail = newEmail;
        this.newName = newName;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public ChangeInfoRequestDto setNewEmail(String newEmail) {
        this.newEmail = newEmail;
        return this;
    }

    public String getNewName() {
        return newName;
    }

    public ChangeInfoRequestDto setNewName(String newName) {
        this.newName = newName;
        return this;
    }
}
