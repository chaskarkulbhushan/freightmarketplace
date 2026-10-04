/*
package com.logix.freightmarketplace.secconfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    // logger
    //private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${spring.app.jwtSecret}")
    String jwtSecret;
    private final long expirationMillis = 86400000;

    // getJwtFromHeader
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // Generate Toke from user name
    public String generateToken(UserDetails userDetails) {
        String username = userDetails.getUsername();
        return Jwts.builder()
                .subject(username)
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .issuedAt(new Date(System.currentTimeMillis()))
                .signWith(key())
                .compact();
    }

    public String generateToken(String email, String role, Long userId) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(key())
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser().verifyWith((SecretKey) key())
                .build().parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        // code to validate authToken
        try {
            */
/*JwtParser parser = *//*
Jwts.parser().setSigningKey(key()).build().parseClaimsJws(authToken);
            //Claims claims = parser.parseClaimsJws(authToken).getBody();
        } catch (Exception e) {
            return false;
        }
        return true;

    }

    public Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(key()).build().parseClaimsJws(token).getPayload();
    }

}*/
