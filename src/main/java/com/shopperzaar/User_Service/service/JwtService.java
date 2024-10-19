package com.shopperzaar.User_Service.service;


import com.shopperzaar.User_Service.dto.JwtPayloadDto;
import com.shopperzaar.User_Service.dto.UserDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.lang.Assert;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.security.interfaces.RSAPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.public.key}")
    RSAPublicKey key;

    @Value("${jwt.private.key}")
    RSAPrivateKey privateKey;

    private static final long TOKEN_EXPIRATION = 20 * 60 * 1000; // 20 minutes

    /**
     * Create JWT token using the user details and private key.
     * The token includes the user's email, roles, and other information if required.
     *
     * @param userDto User information
     * @return Generated JWT token
     */
    public String createToken(UserDto userDto) {
        Instant now = Instant.now();

        return Jwts.builder()
                .subject(userDto.getEmail()) // Use subject to set email
                .claim("roles", userDto.getRoles()) // Add roles as a claim
                .issuedAt(Date.from(now)) // Set the issue time using Date
                .expiration(Date.from(now.plusMillis(TOKEN_EXPIRATION))) // Set expiration
                .signWith(privateKey) // Sign with the RSA private key (no need for the algorithm param)
                .compact();
    }

    public JwtPayloadDto parseToken(String authToken) {
        Jws<Claims> claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(authToken);
        Claims claim = claims.getPayload();
        String email = claim.getSubject();
        //to check (List<String> List<?> or ArrayList?)
        List roles = claim.get("roles", List.class);
        return new JwtPayloadDto(email, roles);
    }

    /**
     * Validate the JWT token signature.
     * Also checks the expiration time and other claims. (throws error in case of invalid token)
     * @param authToken JWT token
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(authToken);
            return true;
        } catch (JwtException ex) {
            log.error("Invalid JWT signature");
            return false;
        }
    }


}
