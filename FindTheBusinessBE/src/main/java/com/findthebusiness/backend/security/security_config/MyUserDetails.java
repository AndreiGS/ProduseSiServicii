package com.findthebusiness.backend.security.security_config;

import com.findthebusiness.backend.entity.Users;
import com.findthebusiness.backend.utils.BasicEncrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class MyUserDetails implements UserDetails {

    private String id;
    private byte[] email;
    private String password;
    private Boolean isActive;
    private Boolean isBanned;
    private List<GrantedAuthority> roles = new ArrayList<>();

    public MyUserDetails() {
    }

    public MyUserDetails(Users user) {
        this.id=user.getId();
        this.email=user.getEmail();
        this.password=user.getPassword();
        this.isActive=user.getActive();
        this.isBanned=user.getBanned();
        roles.add((GrantedAuthority)() -> String.valueOf(user.getUserRole()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return BasicEncrypt.decrypt(email);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isBanned;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    public String getId() {
        return id;
    }
}
