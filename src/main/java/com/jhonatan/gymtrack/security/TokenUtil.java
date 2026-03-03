package com.jhonatan.gymtrack.security;

import com.jhonatan.gymtrack.dto.authDto.TokenDataDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;

@Component
public class TokenUtil {

    public static final String ISSUER = "GymTrack";

    // Puxa o valor configurado no application.properties / variáveis de ambiente
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private long EXPIRATION_TIME;


    public AuthToken encodeToken(TokenDataDTO userData) {
        try {
            Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
            String jwtToken = Jwts.builder()
                    .subject(userData.email())
                    .issuer(ISSUER)
                    .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    //.claim("ROLES",  userData.roles()) -> pra caso eu queira usar roles futuramente
                    .signWith(key)
                    .compact();

            return new AuthToken(jwtToken);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Authentication decodeToken(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null) {
                token = token.replace("Bearer ", "");
                SecretKey secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
                JwtParser parser = Jwts.parser().verifyWith(secretKey).build();
                Claims claims = (Claims) parser.parse(token).getPayload();

                String subject = claims.getSubject();
                String issuer = claims.getIssuer();
                Date exp = claims.getExpiration();

                if (issuer.equals(ISSUER) && !subject.isEmpty() && exp.after(new Date(System.currentTimeMillis()))) {
                    return new UsernamePasswordAuthenticationToken(subject,null,new ArrayList<>());
                }
            }
        } catch (Exception e) {
            System.err.println("invalid Token or expired: " + e.getMessage());
        }
        return null;
    }
}
