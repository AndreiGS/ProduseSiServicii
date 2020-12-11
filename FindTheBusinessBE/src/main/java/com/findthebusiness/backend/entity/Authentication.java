package com.findthebusiness.backend.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Authentication {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private String id;

    @NotNull
    @Column(length = 550)
    private String accessToken;

    @NotNull
    private String refreshToken;

    @NotNull
    private String refreshTokenId;

    @NotNull
    private String csrfToken;

    @NotNull
    private Date date;

    public Authentication() {
    }

    public Authentication(@NotNull String accessToken, @NotNull String refreshToken, @NotNull String refreshTokenId, @NotNull String csrfToken, @NotNull Date date) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.refreshTokenId = refreshTokenId;
        this.csrfToken = csrfToken;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public Authentication setId(String id) {
        this.id = id;
        return this;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Authentication setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Authentication setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public Authentication setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
        return this;
    }

    public String getRefreshTokenId() {
        return refreshTokenId;
    }

    public Authentication setRefreshTokenId(String refreshTokenId) {
        this.refreshTokenId = refreshTokenId;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public Authentication setDate(Date date) {
        this.date = date;
        return this;
    }
}
