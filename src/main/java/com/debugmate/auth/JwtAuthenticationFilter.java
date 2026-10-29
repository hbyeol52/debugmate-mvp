package com.debugmate.auth;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService; this.userDetailsService = userDetailsService;
    }
    @Override protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String token = null;
        if (request.getCookies() != null) for (Cookie cookie : request.getCookies())
            if ("debugmate_token".equals(cookie.getName())) token = cookie.getValue();
        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails details = userDetailsService.loadUserByUsername(jwtService.verifyAndGetEmail(token));
                if (details.isEnabled()) SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities()));
            } catch (Exception ignored) { }
        }
        chain.doFilter(request, response);
    }
}
