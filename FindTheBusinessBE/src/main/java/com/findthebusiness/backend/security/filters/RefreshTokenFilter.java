package com.findthebusiness.backend.security.filters;

import com.findthebusiness.backend.entity.Authentication;
import com.findthebusiness.backend.exception.WrongCredentialsForRequestException;
import com.findthebusiness.backend.repository.AuthenticationRepository;
import com.findthebusiness.backend.security.utils.RefreshTokenUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
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
public class RefreshTokenFilter extends OncePerRequestFilter {
    private final RequestMatcher requireRefreshTokenProtectionMatcher = new RefreshTokenFilter.DefaultRequiresRefreshTokenMatcher();
    private final AccessDeniedHandler accessDeniedHandler = new AccessDeniedHandlerImpl();
    private final RefreshTokenUtil refreshTokenUtil;
    private final AuthenticationRepository authenticationRepository;

    private final String REFRESH_TOKEN = "REFRESH-TOKEN";
    private final String ACCESS_TOKEN = "ACCESS-TOKEN";
    private final String X_REFRESH_TOKEN = "X-REFRESH-TOKEN";

    public RefreshTokenFilter(RefreshTokenUtil refreshTokenUtil, AuthenticationRepository authenticationRepository) {
        this.refreshTokenUtil = refreshTokenUtil;
        this.authenticationRepository = authenticationRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (requireRefreshTokenProtectionMatcher.matches(request)) {
            final String refreshTokenValue = request.getHeader(X_REFRESH_TOKEN);
            final Cookie[] cookies = request.getCookies();

            String refreshTokenCookieValue = null;
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(REFRESH_TOKEN)) {
                        refreshTokenCookieValue = cookie.getValue();
                    }
                }
            }

            if (refreshTokenValue == null || !refreshTokenValue.equals(refreshTokenCookieValue)) {
                Cookie accessToken = new Cookie(ACCESS_TOKEN, "");
                accessToken.setMaxAge(0);
                accessToken.setHttpOnly(true);
                response.addCookie(accessToken);

                accessDeniedHandler.handle(request, response, new AccessDeniedException(
                        "Missing or non-matching CSRF-token"));
                return;
            }

            Authentication auth = authenticationRepository.findByRefreshToken(refreshTokenValue).orElseThrow(WrongCredentialsForRequestException::new);

            if(!refreshTokenUtil.validateToken(refreshTokenValue, auth.getRefreshTokenId())) {
                Cookie accessToken = new Cookie(ACCESS_TOKEN, "");
                accessToken.setMaxAge(0);
                accessToken.setHttpOnly(true);
                response.addCookie(accessToken);

                accessDeniedHandler.handle(request, response, new AccessDeniedException(
                        "Missing or non-matching CSRF-token"));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    public static final class DefaultRequiresRefreshTokenMatcher implements RequestMatcher {
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
            for(String s : urls)
                if(url.equals(s))
                    return true;

            return false;
        }
    }
}
