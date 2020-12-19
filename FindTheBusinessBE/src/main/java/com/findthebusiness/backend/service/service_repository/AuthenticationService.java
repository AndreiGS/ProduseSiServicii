package com.findthebusiness.backend.service.service_repository;

import com.findthebusiness.backend.dto.authentication.*;
import com.findthebusiness.backend.entity.Authentication;
import com.findthebusiness.backend.entity.Shops;
import com.findthebusiness.backend.entity.Users;
import com.findthebusiness.backend.exception.UserAlreadyRegisteredException;
import com.findthebusiness.backend.exception.UserNotRegisteredException;
import org.springframework.http.ResponseEntity;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public interface AuthenticationService {

    //CONTROLLER METHODS
    void delete7DaysOldAuthentications();
    ResponseEntity<?> confirmUser(String code) throws UserNotRegisteredException;
    LoginResponseWithHttpResponseDto loginUser(LoginRequestDto loginRequestDto);
    void registerUser(RegisterRequestDto registerRequestDto)  throws UserAlreadyRegisteredException, IOException, MessagingException;
    Cookie logoutUser(HttpServletRequest httpServletRequest);
    ResponseEntity<?> loginWithFacebook(String storeId, LoginWithFacebookRequestDto loginWithFacebookRequestDto);
    AuthenticationCredentialsDto checkIdentity(HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException;
    ResponseEntity<?> sendResetPasswordEmail(SendResetPasswordEmailRequestDto sendResetPasswordEmailRequestDto) throws Exception;

    //CUSTOM METHODS
    String getCookieByName(HttpServletRequest request, String name);
    boolean isAccessTokenExpired(String token);
    String getIdFromAccessToken(String token);
    Cookie deleteCookie(String cookieName);
    boolean hasUserAlreadyCommented(byte[] userName, String storeId);
    boolean isPasswordOK(String password);

    //JPA METHODS
    void saveUserWithoutReturning(Users user);
    Users saveUserAndReturn(Users user);
    void deleteUser(Users user);
    void updateUserWithoutReturning(Users user);
    Users findUserById(String id);
    Users findUserByEmail(byte[] email);
    void saveAuthenticationDetailsWithoutReturning(Authentication auth);
    void deleteAuthenticationByAccessToken(String accessToken);
    void deleteAuthenticationByDateBefore(Date date);
    Shops findShopById(String shopId);
    Boolean existsCommentByShopAndUsername(Shops shop, byte[] username);
}
