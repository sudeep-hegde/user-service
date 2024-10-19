package com.shopperzaar.User_Service.config;

import com.shopperzaar.User_Service.filter.JwtAuthenticationFilter;
import com.shopperzaar.User_Service.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * This class is used to configure the security of the application.
 * It is annotated with @Configuration to indicate that it is a configuration class.
 * It is annotated with @EnableWebSecurity to enable Spring Security's web security support.
 * ***SecurityConfig to register filters and configure the security of the application.
 * override default security configuration to add custom filters.
 */
@RequiredArgsConstructor
@EnableMethodSecurity
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //exception class for filtering the authentication exceptions
    private final UserAuthenticationEntryPoint userAuthenticationEntryPoint;

    private final JwtService jwtService;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(userAuthenticationEntryPoint)
                )
                .authorizeHttpRequests(
                        authorizeHttp -> {
                            authorizeHttp.requestMatchers("v0/auth/*").permitAll(); // permit the following requests without authentication
                            authorizeHttp.anyRequest().authenticated();  //any request --> pass through the filters
                        }
                )
//                .formLogin(l -> l.defaultSuccessUrl("/private"))
//                .logout(l -> l.logoutSuccessUrl("/"))
//                .oauth2Login(withDefaults())
//                .httpBasic(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtAuthenticationFilter(jwtService),UsernamePasswordAuthenticationFilter.class) // register custom filter in security config.
                .sessionManagement(sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
//                .authenticationProvider(new DanielAuthenticationProvider())
                .build();
    }

}
