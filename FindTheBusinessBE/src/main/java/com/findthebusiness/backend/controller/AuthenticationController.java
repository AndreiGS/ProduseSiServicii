package com.findthebusiness.backend.controller;

import com.findthebusiness.backend.dto.authentication.*;
import com.findthebusiness.backend.exception.UserAlreadyRegisteredException;
import com.findthebusiness.backend.security.utils.AuthenticationUtil;
import com.findthebusiness.backend.service.service_implementation.AuthenticationServiceImpl;
import com.findthebusiness.backend.utils.ScheduleDelayTimes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {

    private final AuthenticationServiceImpl authenticationService;

    private static final Logger log = LoggerFactory.getLogger(ShopController.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private final Integer scheduleDelay=1000*60*24*7;

    public AuthenticationController(AuthenticationServiceImpl authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/admin")
    private String admin() {
        return "admin";
    }

    @GetMapping("/user")
    private String user() {
        return "user";
    }

    @PatchMapping("/confirmEmail")
    private ResponseEntity<?> confirmEmail(@RequestParam String code) {
        return authenticationService.confirmUser(code);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    private void registerUser(@RequestBody RegisterRequestDto registerRequestDto) throws UserAlreadyRegisteredException, IOException, MessagingException {
        authenticationService.registerUser(registerRequestDto);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    private ResponseEntity<LoginResponseDto> loginUser(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        LoginResponseWithHttpResponseDto loginResponseWithHttpResponseDto = authenticationService.loginUser(loginRequestDto);
        response.addCookie(loginResponseWithHttpResponseDto.getAccessToken());
        return ResponseEntity.ok(new LoginResponseDto(loginResponseWithHttpResponseDto.getRefreshToken(), loginResponseWithHttpResponseDto.getCsrfToken(), loginResponseWithHttpResponseDto.getUserRole()));
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    private ResponseEntity<LoginResponseDto> logoutUser(HttpServletRequest request, HttpServletResponse response) {
        try {
            Cookie deletedAccessTokenCookie = authenticationService.logoutUser(request);
            response.addCookie(deletedAccessTokenCookie);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/loginWithFacebook")
    private ResponseEntity<?> loginUser(@RequestParam("store") String storeId, @RequestBody LoginWithFacebookRequestDto loginWithFacebookRequestDto) {
        try {
            return authenticationService.loginWithFacebook(storeId, loginWithFacebookRequestDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping("/checkIdentity")
    private ResponseEntity<?> checkIdentity(HttpServletRequest request, HttpServletResponse response) {
        try {
            AuthenticationCredentialsDto auth = authenticationService.checkIdentity(request);
            response.addCookie(auth.getAccessToken());

            return ResponseEntity.ok(new CheckIdentityResponseDto(auth.getRefreshToken(), auth.getCsrfToken()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Scheduled(fixedDelay = ScheduleDelayTimes.DELETE_OLD_AUTHS)
    private void delete7DaysOldAuthentications() {
        log.info("Delete old authentication. The time is now {}", dateFormat.format(new Date()));
        try {
            authenticationService.delete7DaysOldAuthentications();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
