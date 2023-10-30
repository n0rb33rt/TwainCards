package com.norbert.customer.authentication;


import com.norbert.customer.jwt.*;
import com.norbert.customer.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${jwt.secret.key}")
    private String SECRET_KEY;
    private final static Integer BEARER_PREFIX_LENGTH = 7;
    private final static String BEARER_PREFIX = "Bearer ";
    private final JwtTokenService jwtTokenService;

    private final static long HOUR = 1000*60*60;
    public boolean isTokenValid(String token)  {
        return !isTokenRevoked(token) &&  !isTokenExpired(token);
    }

    private boolean isTokenRevoked(String token) {
        return jwtTokenService.findByToken(token).isRevoked();
    }
    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date(System.currentTimeMillis()));
    }
    private Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }
    public String generateToken(User user){
        return generateToken(new HashMap<>(),user);
    }
    public String generateToken(
            Map<String,Object> extraClaims,
            User user
    ){
        String jwtToken = Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+HOUR))
                .signWith(getSignInKey(), SignatureAlgorithm.HS512)
                .compact();
        jwtTokenService.save(JwtToken.builder()
                        .token(jwtToken)
                        .revoked(false)
                        .user(user)
                        .build());
        return jwtToken;
    }
    public String extractEmail(String token){
        return extractClaim(token,Claims::getSubject);
    }
    private <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isAuthHeaderValid(String authHeader){
        return authHeader == null || !authHeader.startsWith(BEARER_PREFIX);
    }
    public String extractJwtTokenFromHeader(String authHeader){
        return authHeader.substring(BEARER_PREFIX_LENGTH);
    }
}
