package com.ayed.booknetwork.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service

public class JwtService {
   @Value("${application.security.jwt.secret-key}")
    private String secretKey;
   @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    public String extractUsername(String token) // here we extract the username from the token
    {
        return extractClaim(token, Claims::getSubject); // we use getSubject to get the username from the token
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) // here we extract the claims from the token
            // we use this function to extract the claims from the token and this function will be used in the extractAllClaims function
    {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims); // we apply the function on the claims to get the result
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,// extra claims that we want to add to the token
            UserDetails userDetails // the user that we want to generate the token for
    ) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        var authorities = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return Jwts
                .builder()
                .setClaims(extraClaims) // the extra claims that we want to add to the token
                .setSubject(userDetails.getUsername())// the user id of my token
                .setIssuedAt(new Date(System.currentTimeMillis())) // the time that the token is issued
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // the time that the token will expire
                .claim("authorities", authorities) // the roles of the user
                .signWith(getSignInKey())// we need to sign the token with the secret key
                .compact(); // we compact the token to a string
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); // we decode the secret key to bytes , we use byte[] because the key should be in bytes
        return Keys.hmacShaKeyFor(keyBytes); // we create a key from the bytes that we decoded from the secret key   .
}
}
