package com.findthebusiness.backend.security.filters;

import com.findthebusiness.backend.entity.Authentication;
import com.findthebusiness.backend.exception.WrongCredentialsForRequestException;
import com.findthebusiness.backend.repository.AuthenticationRepository;
import com.findthebusiness.backend.repository.UserRepository;
import com.findthebusiness.backend.security.security_config.MyUserDetails;
import com.findthebusiness.backend.security.security_config.MyUserDetailsService;
import com.findthebusiness.backend.security.utils.AccessTokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final MyUserDetailsService userDetailsService;
    private final AccessTokenUtil accessTokenUtil;
    private final UserRepository userRepository;
    private final AuthenticationRepository authenticationRepository;

    private final String ACCESS_TOKEN = "ACCESS-TOKEN";
    private final String REFRESH_TOKEN = "REFRESH-TOKEN";
    private final String CSRF_TOKEN = "CSRF-TOKEN";

    private final RequestMatcher requiresAccessTokenMatcher = new JwtRequestFilter.DefaultRequiresAccessTokenMatcher();
    private final AccessDeniedHandler accessDeniedHandler = new AccessDeniedHandlerImpl();

    public JwtRequestFilter(MyUserDetailsService userDetailsService, AccessTokenUtil jwtUtil, UserRepository userRepository, AuthenticationRepository authenticationRepository) {
        this.userDetailsService = userDetailsService;
        this.accessTokenUtil = jwtUtil;
        this.userRepository = userRepository;
        this.authenticationRepository = authenticationRepository;
    }

    private String isAuthenticationWhiteListedReturnsAccessToken(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();

        String csrfToken = null, refreshToken = null, accessToken = null;

        for (Cookie cookie : cookies) {
            if (cookie == null)
                break;

            switch (cookie.getName()) {
                case CSRF_TOKEN:
                    csrfToken = cookie.getValue();
                    break;
                case ACCESS_TOKEN:
                    accessToken = cookie.getValue();
                    break;
                case REFRESH_TOKEN:
                    refreshToken = cookie.getValue();
                    break;
            }
        }

        if (csrfToken == null || refreshToken == null || accessToken == null)
            return null;

        Authentication auth = authenticationRepository.findByAccessTokenEqualsAndCsrfTokenEqualsAndRefreshTokenEquals(accessToken, csrfToken, refreshToken).orElseThrow(WrongCredentialsForRequestException::new);
        if(auth == null)
            return null;
        return accessToken;
    }

    private void authenticateNonExpiredToken(String userId, String accessToken, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            MyUserDetails userDetails = (MyUserDetails) this.userDetailsService.loadUserByUsername(userId);
            if(!accessTokenUtil.validateToken(accessToken, userDetails)) {
                Cookie accessTokenCookie = new Cookie(ACCESS_TOKEN, "");
                accessTokenCookie.setMaxAge(0);
                accessTokenCookie.setHttpOnly(true);
                response.addCookie(accessTokenCookie);

                accessDeniedHandler.handle(request, response, new AccessDeniedException(
                        "Access token not valid"));
                return;
            }
            authenticate(userDetails, request);
        } else {
            Cookie accessTokenCookie = new Cookie(ACCESS_TOKEN, "");
            accessTokenCookie.setMaxAge(0);
            accessTokenCookie.setHttpOnly(true);
            response.addCookie(accessTokenCookie);

            accessDeniedHandler.handle(request, response, new AccessDeniedException(
                    "Access token not valid"));
            return;
        }
    }

    private void authenticateExpiredToken(String userId, String refreshToken, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            String refreshTokenFromCookie = null;

            Cookie[] cookies = request.getCookies();
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals(REFRESH_TOKEN)) {
                    refreshTokenFromCookie = cookie.getValue();
                    break;
                }
            }

            if(!refreshToken.equals(refreshTokenFromCookie)) {
                Cookie accessTokenCookie = new Cookie(ACCESS_TOKEN, "");
                accessTokenCookie.setMaxAge(0);
                accessTokenCookie.setHttpOnly(true);
                response.addCookie(accessTokenCookie);

                accessDeniedHandler.handle(request, response, new AccessDeniedException(
                        "RefreshToken not valid"));
                return;
            }

            MyUserDetails userDetails = (MyUserDetails) this.userDetailsService.loadUserByUsername(userId);
            authenticate(userDetails, request);
        } else {
            Cookie accessTokenCookie = new Cookie(ACCESS_TOKEN, "");
            accessTokenCookie.setMaxAge(0);
            accessTokenCookie.setHttpOnly(true);
            response.addCookie(accessTokenCookie);

            accessDeniedHandler.handle(request, response, new AccessDeniedException(
                    "Access token not valid"));
            return;
        }
    }

    private void authenticate(MyUserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        usernamePasswordAuthenticationToken
                .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        if (requiresAccessTokenMatcher.matches(request)) {
            String accessToken = isAuthenticationWhiteListedReturnsAccessToken(request);
            if (accessToken == null) {
                Cookie accessTokenCookie = new Cookie(ACCESS_TOKEN, "");
                accessTokenCookie.setMaxAge(0);
                accessTokenCookie.setHttpOnly(true);
                response.addCookie(accessTokenCookie);

                accessDeniedHandler.handle(request, response, new AccessDeniedException(
                        "Access token not valid"));
                return;
            }

            String userId;
            try {
                userId = accessTokenUtil.extractId(accessToken);
                authenticateNonExpiredToken(userId, accessToken, request, response);
            } catch (ExpiredJwtException e) {
                userId = e.getClaims().getSubject();
                Claims tokenClaims = e.getClaims();
                String refreshToken = tokenClaims.get("refresh_token", String.class);
                authenticateExpiredToken(userId, refreshToken, request, response);
            }
        }
        chain.doFilter(request, response);
    }

    public static final class DefaultRequiresAccessTokenMatcher implements RequestMatcher {
        String[] urls = {
                "/api/authentication/user",
                "/api/authentication/admin",
                "/api/authentication/logout",
                "/api/authentication/checkIdentity",

                "/api/user/getUserProfileInfo",
                "/api/user/changeInfo",
                "/api/user/changePassword",
                "/api/user/sendEmailForChangingPassword",
                "/api/user/sendEmailForChangingInformation",
                "/api/user/charge",
                "/api/user/checkIfCanAddItem",
                "/api/user/addShopToken",
                "/api/user/deleteProfile",

                "/api/shops/refreshShop",
                "/api/shops/promoteShop",
                "/api/shops/saveShop",
                "/api/shops/deleteShop",
                "/api/shops/checkIfOwner",
                "/api/shops/changeContactData",
                "/api/shops/changePublished",
                "/api/shops/changeHasAutomaticTokenRefresh",
                "/api/shops/changeLargeImage",

                "/api/tabs/postTab",
                "/api/tabs/deleteTab",

                "/api/items/deleteItem",
                "/api/items/addItem"
        };

        @Override
        public boolean matches(HttpServletRequest request) {
            String url = request.getRequestURI();
            for (String s : urls)
                if (url.equals(s))
                    return true;

            return false;
        }
    }
}
