package com.findthebusiness.backend.security.security_config;

import com.findthebusiness.backend.security.filters.JwtRequestFilter;
import com.findthebusiness.backend.security.filters.RefreshTokenFilter;
import com.findthebusiness.backend.security.filters.StatelessCSRFFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@EnableWebSecurity
public class SpringSecurityAuth extends WebSecurityConfigurerAdapter {

    private final MyUserDetailsService userDetailsService;

    private final JwtRequestFilter jwtRequestFilter;
    private final StatelessCSRFFilter statelessCSRFFilter;
    private final RefreshTokenFilter refreshTokenFilter;

    public SpringSecurityAuth(@Qualifier("myUserDetailsService") MyUserDetailsService userDetailsService, JwtRequestFilter jwtRequestFilter, StatelessCSRFFilter statelessCSRFFilter, RefreshTokenFilter refreshTokenFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
        this.statelessCSRFFilter = statelessCSRFFilter;
        this.refreshTokenFilter = refreshTokenFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors()
                .and()
                .addFilterBefore(refreshTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(statelessCSRFFilter, CsrfFilter.class)
                .authorizeRequests()
                .antMatchers("/api/authentication/admin").hasRole("ADMIN")
                .antMatchers("/api/authentication/user",
                            "/api/authentication/checkIdentity",
                            //"/api/authentication/logout",

                            "/api/user/getUserProfileInfo",
                            "/api/user/deleteProfile",
                            "/api/user/sendEmailForChangingInformation",
                            "/api/user/changeInfo",
                            "/api/user/sendEmailForChangingPassword",
                            "/api/user/charge",
                            "/api/user/checkIfCanAddItem",
                            "/api/user/addShopToken",

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
                        ).hasAnyRole("USER", "ADMIN")
                .anyRequest().permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        String frontendUrl1 = System.getenv("SPRING_APP_FRONTEND_1");
        String frontendUrl2 = System.getenv("SPRING_APP_FRONTEND_2");

        List<String> frontendUrls = new ArrayList<>();
        if(frontendUrl1 != null) {
            frontendUrls.add(frontendUrl1);
        }

        if(frontendUrl2 != null) {
            frontendUrls.add(frontendUrl2);
        }

        if(frontendUrl1 == null && frontendUrl2 == null) {
            frontendUrls.add("http://localhost:8081");
            frontendUrls.add("http://127.0.0.1:8887");
        }

        configuration.setAllowedOrigins(frontendUrls);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("content-type", "X-CSRF-TOKEN", "X-REFRESH-TOKEN", "Set-Cookie"));
        configuration.setExposedHeaders(Arrays.asList("X-CSRF-TOKEN", "X-REFRESH-TOKEN"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
