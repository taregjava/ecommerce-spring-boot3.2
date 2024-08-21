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
    private static final int TOKEN_VALIDITY = 3600 * 5;

    public String generateToken(UserDetails userDetails) {
        logger.debug("Generating token for user: {}", userDetails.getUsername());
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }
    // New method to generate token with extra claims
    public String generateToken(UserDetails userDetails, Map<String, Object> extraClaims) {
        logger.debug("Generating token for user: {} with extra claims", userDetails.getUsername());
        return createToken(extraClaims, userDetails.getUsername());
    }
    private String createToken(Map<String, Object> claims, String subject) {
        logger.debug("Creating token with subject: {}", subject);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
                .signWith(SECRET_KEY)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        logger.debug("Validating token for user: {}", userDetails.getUsername());
        final String username = extractUsername(token);
        boolean isValid = (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        logger.debug("Is token valid: {}", isValid);
        return isValid;
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
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
    }
}
