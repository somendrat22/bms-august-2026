package com.acciojobs.bms_august.securites;

import com.acciojobs.bms_august.constants.SystemConstant;
import com.acciojobs.bms_august.models.Role;
import com.acciojobs.bms_august.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Component
public class JwtUtility {

    // Secret Password + Algorithm
    public Key key = Keys.hmacShaKeyFor(SystemConstant.JWT_SECRET_PASSWORD.getBytes());

    public String generateJwtToken(User user){
        // Claim-1 -> User Email
        // Claim-2 -> List<String> roleNames
        String email = user.getEmail();
        List<String> roleNames = user.getRoles().stream()
                .map(Role::getRoleName)
                .toList();
        // Algorithm + Secret password
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("roleNames", roleNames);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + SystemConstant.EXPIRATION_TIME_IN_MILLIS))
                .setIssuedAt(new Date())
                .signWith(key)
                .compact();

    }

    public Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
