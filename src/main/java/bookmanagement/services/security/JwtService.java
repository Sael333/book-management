package bookmanagement.services.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import bookmanagement.model.request.JwtDataRequest;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret}")
    private String jwtSecret;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    // Método para generar el token JWT
    public String generateToken(JwtDataRequest jwtData) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", jwtData.getUserId());
        claims.put("confirmPayment", jwtData.isPaymentConfirm());
        claims.put("amount", jwtData.getAmount());
        return Jwts.builder()
                .subject(jwtData.getUserId())
                .claims(claims)  // Aquí se puede poner un ID único o cualquier dato relevante
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))  // Expira en 1 hora
                .signWith(SignatureAlgorithm.HS256, getSecretKey())
                .compact();
    }

    // Validar el token JWT
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(getSecretKey())  // Establecer la clave secreta
                    .build()                   // Construir el parser
                    .parseClaimsJws(token)     // Validar y parsear el JWT con firma
                    .getBody();
            return true;  // Si no se lanza excepción, el token es válido
        } catch (ExpiredJwtException e) {
            // El token ha expirado
            System.out.println("El token ha expirado");
            return false;
        } catch (SignatureException e) {
            // La firma no es válida
            System.out.println("La firma del token no es válida");
            return false;
        } catch (JwtException e) {
            // El token es inválido por alguna otra razón
            System.out.println("Token inválido");
            return false;
        }
    }

    public String getUserIdFromToken(String token) {
        return extractAllClaims(token)
                .getSubject();  // 'subject' sería el 'userId' (email)
    }

    public Boolean getConfirmPaymentFromToken(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("confirmPayment", Boolean.class);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSecretKey())
                .build()                   // Construir el parser
                .parseClaimsJws(token)     // Validar y parsear el JWT con firma
                .getBody();
    }
}

