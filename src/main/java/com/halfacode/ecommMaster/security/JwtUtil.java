package com.halfacode.ecommMaster.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    // Generate a secure key for HS512
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static final SecretKey REFRESH_SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static final int TOKEN_VALIDITY = 3600 * 5; // 5 hours
    private static final int REFRESH_TOKEN_VALIDITY = 3600 * 24 * 7; // 1 week

    public String generateToken(UserDetails userDetails) {
        logger.debug("Generating token for user: {}", userDetails.getUsername());
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername(), TOKEN_VALIDITY, SECRET_KEY);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        logger.debug("Generating refresh token for user: {}", userDetails.getUsername());
        return createToken(new HashMap<>(), userDetails.getUsername(), REFRESH_TOKEN_VALIDITY, REFRESH_SECRET_KEY);
    }

    public String generateToken(UserDetails userDetails, Map<String, Object> extraClaims) {
        logger.debug("Generating token for user: {} with extra claims", userDetails.getUsername());
        return createToken(extraClaims, userDetails.getUsername(), TOKEN_VALIDITY, SECRET_KEY);
    }

    private String createToken(Map<String, Object> claims, String subject, int validity, SecretKey key) {
        logger.debug("Creating token with subject: {}", subject);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validity * 1000))
                .signWith(key)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        logger.debug("Validating token for user: {}", userDetails.getUsername());
        final String username = extractUsername(token);
        boolean isValid = (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        logger.debug("Is token valid: {}", isValid);
        return isValid;
    }

    public Boolean validateRefreshToken(String token) {
        logger.debug("Validating refresh token");
        try {
            // Use REFRESH_SECRET_KEY for validation
            extractAllClaims(token, REFRESH_SECRET_KEY);
            return true;
        } catch (Exception e) {
            logger.error("Invalid refresh token: {}", e.getMessage());
            return false;
        }
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token, SECRET_KEY);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token, SecretKey key) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}