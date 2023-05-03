package com.onlinebookstore.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

/**
 * Даний клас JwtTokenService є сервісом, який надає різноманітні методи для генерації та перевірки JWT токенів.
 * В класі JwtTokenService містяться наступні методи:
 * - extactEmail: метод, який отримує токен та повертає адресу електронної пошти (email), яка зберігається в токені.
 * - generateToken: метод, який створює JWT токен з використанням об'єкта CustomUserDetails (який містить дані про користувача) та повертає згенерований токен.
 * - isTokenValid: метод, який перевіряє чи є дійсним JWT токен та чи збігаються дані користувача з тими, що зберігаються у токені.
 * - isTokenExpired: метод, який перевіряє чи не минув термін дії JWT токену.
 * - extractExpiration: метод, який отримує JWT токен та повертає дату закінчення дії токену.
 * - extractClaim: метод, який отримує JWT токен та функцію claimsResolver, яка дозволяє отримати потрібний клейм (claim) з токену.
 * - extractAllClaims: метод, який отримує JWT токен та повертає всі клейми (claims) з токену.
 * - getSignInKey: метод, який повертає ключ для підпису JWT токену з використанням алгоритму HS256.
 * <p>
 * Клас JwtTokenService використовується в JwtAuthenticationFilter для перевірки дійсності та розшифрування JWT токену.
 */

@Service
public class JwtTokenService {

    // private static final String SECRET_KEY = "cdthlkjdcr11ktybyuhfl88";

    public String extactEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(CustomUserDetails userDetails) {
        return Jwts.builder()
                //.setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, getSignInKey())
                .compact();
    }

    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = extactEmail(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {

        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }

    private Key getSignInKey() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

}
