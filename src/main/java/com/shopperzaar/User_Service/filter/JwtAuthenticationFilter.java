package com.shopperzaar.User_Service.filter;


import com.shopperzaar.User_Service.dto.JwtPayloadDto;
import com.shopperzaar.User_Service.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<String> tokenOptional = getJwtTokenFromRequest(request);
        if (tokenOptional.isPresent()) {
            String token = tokenOptional.get();
            if(jwtService.validateToken(token)) {
                Optional<UserDetails> userDetailsOptional = getUserDetails(jwtService.parseToken(token));
                //Set the authentication object in the SecurityContext and proceed with the filter chain.
                userDetailsOptional.ifPresent(userDetails -> setAuthentication(request, userDetails));
                log.info("User details: {}", userDetailsOptional.get());
                log.info("Authorities: {}", userDetailsOptional.get().getAuthorities());
            }
        }
        //if authentication is not set, then the filter will fail for usernamePasswordAuthenticationToken
        filterChain.doFilter(request, response);
    }

    private Optional<UserDetails> getUserDetails(JwtPayloadDto jwtPayloadDto) {
        return Optional.of(new org.springframework.security.core.userdetails.User(
                jwtPayloadDto.getEmail(),
                "",
                jwtPayloadDto.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList())));
    }
    private void setAuthentication(HttpServletRequest request, UserDetails userDetails) {
        //set the authentication object in the SecurityContext otherwise the filter will fail.
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }


    /**
     * Extracts the JWT token from the request header
     * if not present return empty string.
     * @param request
     * @return
     */
    private Optional<String> getJwtTokenFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            return Optional.empty();
        }
        return Optional.of(authHeader.substring(7));
    }

}
