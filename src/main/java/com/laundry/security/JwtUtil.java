//package com.laundry.security;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//
//import java.security.Key;
//import java.util.Date;
//
//public class JwtUtil {
//
//    private static final String SECRET =
//            "mysecretkeymysecretkeymysecretkey12345";
//
//    private static final Key key =
//            Keys.hmacShaKeyFor(SECRET.getBytes());
//
//    public static String generateToken(String email) {
//
//        return Jwts.builder()
//                .setSubject(email)
//                .setIssuedAt(new Date())
//                .setExpiration(
//                    new Date(System.currentTimeMillis() + 1000 * 60 * 60)
//                )
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    public static String extractEmail(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//    }
//}
package com.laundry.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import com.laundry.model.UserRole;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET = "laundrySecretKeyLaundrySecretKey12345";
    private final long EXPIRATION = 1000 * 60 * 60 * 24; // 1 day

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateToken(String email,  UserRole role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
 // Extract email (subject)
    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    
 // Extract role
    public String extractRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }
}

