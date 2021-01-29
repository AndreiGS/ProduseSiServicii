package com.findthebusiness.backend.security.utils;

import com.findthebusiness.backend.dto.authentication.AuthenticationCredentialsDto;
import com.findthebusiness.backend.entity.Authentication;
import com.findthebusiness.backend.entity.Users;
import com.findthebusiness.backend.exception.WrongCredentialsForRequestException;
import com.findthebusiness.backend.repository.AuthenticationRepository;
import com.findthebusiness.backend.security.security_config.MyUserDetails;
import com.findthebusiness.backend.utils.UUIDGeneratorUtil;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Component
public class AuthenticationUtil {

    private final AccessTokenUtil accessTokenUtil;
    private final RefreshTokenUtil refreshTokenUtil;
    private final AuthenticationRepository authenticationRepository;

    private final String ACCESS_TOKEN = "ACCESS-TOKEN";
    private final String REFRESH_TOKEN = "REFRESH-TOKEN";
    private final String CSRF_TOKEN = "CSRF-TOKEN";

    public AuthenticationUtil(AccessTokenUtil accessTokenUtil, RefreshTokenUtil refreshTokenUtil, AuthenticationRepository authenticationRepository) {
        this.accessTokenUtil = accessTokenUtil;
        this.refreshTokenUtil = refreshTokenUtil;
        this.authenticationRepository = authenticationRepository;
    }

    public AuthenticationCredentialsDto createCredentials(Users user, String previousAccessToken) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String refreshTokenId = UUIDGeneratorUtil.generateUniqueKeysWithUUIDAndMessageDigest();
        String refreshToken = refreshTokenUtil.generateRefreshToken(refreshTokenId, 60*24*7);
        String accessToken = accessTokenUtil.generateAccessToken(new MyUserDetails(user), 10, refreshToken);
        String csrfToken = UUIDGeneratorUtil.generateUniqueKeysWithUUIDAndMessageDigest();

        if(previousAccessToken == null)
            saveAuthenticationDetailsWithoutReturning(new Authentication(accessToken, refreshToken, refreshTokenId, csrfToken, new Date()));
        else {
            Authentication previousAuth = findAuthenticationByAccessToken(previousAccessToken);
            previousAuth.setRefreshToken(refreshToken);
            previousAuth.setAccessToken(accessToken);
            previousAuth.setCsrfToken(csrfToken);
            previousAuth.setRefreshTokenId(refreshTokenId);
            previousAuth.setDate(new Date());

            saveAuthenticationDetailsWithoutReturning(previousAuth);
        }
        boolean isSecure = true;
        String frontendUrlEnvVar = System.getenv("SPRING_APP_FRONTEND_1");

        if(frontendUrlEnvVar == null)
            isSecure = false;
        else if(!frontendUrlEnvVar.contains("https"))
            isSecure = false;

        Cookie accessTokenCookie = createCookie(ACCESS_TOKEN, accessToken, 7, true, isSecure);
        /*Cookie refreshTokenCookie = createCookie(REFRESH_TOKEN, refreshToken, 7, false, false);
            Cookie csrfTokenCookie = createCookie(CSRF_TOKEN, csrfToken, 7, false, false);*/

        return new AuthenticationCredentialsDto(accessTokenCookie, refreshToken, csrfToken);
    }

    private Cookie createCookie(String name, String value, Integer timeInDays, boolean isHttpOnly, boolean isSecure) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(isHttpOnly);
        cookie.setSecure(isSecure);
        cookie.setMaxAge(1000*60*60*24*timeInDays);
        cookie.setPath("/api");
        return cookie;
    }

    private void saveAuthenticationDetailsWithoutReturning(Authentication auth) {
        authenticationRepository.save(auth);
    }

    private Authentication findAuthenticationByAccessToken(String accessToken) {
        return authenticationRepository.findByAccessToken(accessToken).orElseThrow(WrongCredentialsForRequestException::new);
    }

}
