package com.norbert.notification.jwt;

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

    public String generateToken(String email){
        return generateToken(new HashMap<>(),email);
    }

    public String generateToken(
            Map<String,Object> extraClaims,
            String email
    ){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(email)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*24))
                .compact();
    }

    public boolean isTokenExpired(String jwtToken){
        return extractExpiration(jwtToken).before(new Date(System.currentTimeMillis()));
    }
    private Date extractExpiration(String jwtToken){
        return extractClaim(jwtToken,Claims::getExpiration);
    }
    public String extractEmail(String jwtToken){
        return extractClaim(jwtToken,Claims::getSubject);
    }
    private <T> T extractClaim(String jwtToken, Function<Claims,T> claimsResolver){
        return claimsResolver.apply(extractAllClaims(jwtToken));
    }
    private Claims extractAllClaims(String jwtToken){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }
    private Key getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
