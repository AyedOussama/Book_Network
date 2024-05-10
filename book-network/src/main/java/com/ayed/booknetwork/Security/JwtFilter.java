package com.ayed.booknetwork.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
// if we want to transform this class to a filter we need to extend OncePerRequestFilter
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;// that's why we use  to inject the dependency and create the constructor for us with final word
    private final UserDetailsService userDetailsService;// that's why we use construction to inject the dependency and create the constructor for us with final word

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // this method will be called for every request that comes to the server
        if (request.getServletPath().contains("/api/v1/auth")) {
            // in file Security-config ( /auth/**)  we allowed this path to be accessed without authentication
            // all the requests that come to the server that contains /api/v1/auth will be allowed to pass without authentication
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader("Authorization"); // we get the header of the request
        final String jwt;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer "))  // if the header is null ou it does not start with Bearer
        {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);// we get the token from the header after the word Bearer+space that is why we start from 7
        userEmail = jwtService.extractUsername(jwt); // retrieve the username from the token
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null)
            // if the username is not null and the authentication is null
        {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail); // we load the user from the database by the username

            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities() // we set the authorities of the user
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request) // we set the details of the authentication
                );
                SecurityContextHolder.getContext().setAuthentication(authToken); // we set the authentication in the security context
            }
        }
        filterChain.doFilter(request, response);
        // we pass the request and the response to the next filter until it reaches the controller and if the token is not valid the controller will return an error
        //because filter continues to the next filter
    }
}
