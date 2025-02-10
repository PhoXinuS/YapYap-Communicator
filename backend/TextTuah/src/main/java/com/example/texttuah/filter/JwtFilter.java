package com.example.texttuah.filter;

import com.example.texttuah.config.UserDetailsServiceImpl;
import com.example.texttuah.service.JWTService;
import com.example.texttuah.service.RequestService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.annotations.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserDetailsServiceImpl userDetailsService;
    private final RequestService requestService;

    @Autowired
    public JwtFilter(JWTService jwtService,
                     @Lazy UserDetailsServiceImpl userDetailsService,
                     RequestService requestService
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.requestService = requestService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        if ("WebSocket".equals(request.getHeader("Upgrade"))) {
            filterChain.doFilter(request, response); // Don't apply JWT filter on WebSocket upgrade requests
            return;
        }
        String token = requestService.getJwtToken(request);
        String email = null;
        if (token != null) {
            email = jwtService.extractEmail(token);
        }


        if (email != null
                // Checking if user is authenticated
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Email is not null and user is unauthorized

            // Email is a username
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            if (jwtService.validateToken(token, userDetails)) {
                // Token is valid

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // Adding request to details
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
