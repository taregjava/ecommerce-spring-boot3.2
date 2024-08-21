package com.halfacode.ecommMaster.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

   /* @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;

        log.info("Starting doFilterInternal for request: " + request.getRequestURI());

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7);
            log.info("JWT Token found: " + jwtToken);
            try {
                username = jwtUtil.extractUsername(jwtToken);
                log.info("Username extracted: " + username);
            } catch (Exception e) {
                log.error("Error extracting username from token", e);
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.info("Authentication is null, proceeding to load user details");
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwtToken, userDetails)) {
                log.info("Token validated, setting security context");
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        log.info("Proceeding with the filter chain");
        chain.doFilter(request, response);
    }
*/
   @Override
   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
           throws ServletException, IOException {
       String authorizationHeader = request.getHeader("Authorization");
       String username = null;
       String jwtToken = null;

       if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
           jwtToken = authorizationHeader.substring(7);
           try {
               username = jwtUtil.extractUsername(jwtToken);
           } catch (Exception e) {
               // Handle token extraction errors
               e.printStackTrace();
           }
       }

       if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
           UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
           if (jwtUtil.validateToken(jwtToken, userDetails)) {
               UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                       userDetails, null, userDetails.getAuthorities());
               SecurityContextHolder.getContext().setAuthentication(authentication);
           }
       }

       chain.doFilter(request, response);
   }

}

