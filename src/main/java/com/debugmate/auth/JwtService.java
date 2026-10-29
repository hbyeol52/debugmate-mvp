package com.debugmate.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.debugmate.user.UserAccount;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {
    private final Algorithm algorithm;
    private final long expirationSeconds;
    public JwtService(@Value("${app.jwt.secret}") String secret,
                      @Value("${app.jwt.expiration-seconds}") long expirationSeconds) {
        this.algorithm = Algorithm.HMAC256(secret);
        this.expirationSeconds = expirationSeconds;
    }
    public String issue(UserAccount user) {
        Instant now = Instant.now();
        return JWT.create().withSubject(user.getEmail()).withClaim("role", user.getRole().name())
                .withIssuedAt(Date.from(now)).withExpiresAt(Date.from(now.plusSeconds(expirationSeconds)))
                .sign(algorithm);
    }
    public String verifyAndGetEmail(String token) {
        DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
        return jwt.getSubject();
    }
}
