package com.findthebusiness.backend.security.filters;

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
import java.util.regex.Pattern;

@Component
public class StatelessCSRFFilter extends OncePerRequestFilter {
    private static final String CSRF_TOKEN = "CSRF-TOKEN";
    private static final String X_CSRF_TOKEN = "X-CSRF-TOKEN";
    private final RequestMatcher requireCsrfProtectionMatcher = new DefaultRequiresCsrfMatcher();
    private final AccessDeniedHandler accessDeniedHandler = new AccessDeniedHandlerImpl();

    private final String ACCESS_TOKEN = "ACCESS-TOKEN";
    private final String REFRESH_TOKEN = "REFRESH-TOKEN";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if(requireCsrfProtectionMatcher.matches(request)) {
            final String csrfTokenValue = request.getHeader(X_CSRF_TOKEN);
            final Cookie[] cookies = request.getCookies();

            String csrfCookieValue = null;
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(CSRF_TOKEN)) {
                        csrfCookieValue = cookie.getValue();
                    }
                }
            }

            if (csrfTokenValue == null || !csrfTokenValue.equals(csrfCookieValue)) {
                Cookie accessToken = new Cookie(ACCESS_TOKEN, "");
                accessToken.setMaxAge(0);
                accessToken.setHttpOnly(true);

                String SPRING_ENV = System.getenv("SPRING_ENV");
                if(SPRING_ENV != null && SPRING_ENV.equals("prod")) {
                    accessToken.setDomain("produsesiservicii.ro");
                    accessToken.setSecure(true);
                } else {
                    accessToken.setDomain("localhost");
                    accessToken.setSecure(false);
                }

                response.addCookie(accessToken);
                accessDeniedHandler.handle(request, response, new AccessDeniedException(
                        "Missing or non-matching CSRF-token"));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    public static final class DefaultRequiresCsrfMatcher implements RequestMatcher {
        private final Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");

        String[] urls = {
                "/api/authentication/user",
                "/api/authentication/admin",
                "/api/authentication/register",
                "/api/authentication/login",
                "/api/authentication/logout",
                "/api/authentication/confirmEmail",
                "/api/authentication/loginWithFacebook",
                "/api/authentication/checkIdentity",
                "/api/authentication/sendResetPasswordEmail",

                "/api/user/getUserProfileInfo",
                "/api/user/changeInfo",
                "/api/user/changePassword",
                "/api/user/sendEmailForChangingPassword",
                "/api/user/sendEmailForChangingInformation",
                "/api/user/charge",
                "/api/user/checkIfCanAddItem",
                "/api/user/addShopToken",
                "/api/user/deleteProfile",

                "/api/comments/postComment",

                "/api/shops/refreshShop",
                "/api/shops/promoteShop",
                "/api/shops/saveShop",
                "/api/shops/deleteShop",
                "/api/shops/checkIfOwner",
                "/api/shops/changeContactData",
                "/api/shops/changePublished",
                "/api/shops/changeContactData",
                "/api/shops/changeHasAutomaticTokenRefresh",
                "/api/shops/changeLargeImage",

                "/api/tabs/postTab",
                "/api/tabs/deleteTab",

        };

        @Override
        public boolean matches(HttpServletRequest request) {
            String url = request.getRequestURI();

            for(String s : urls)
                if(url.equals(s))
                    return !allowedMethods.matcher(request.getMethod()).matches();
            return false;
        }
    }
}
