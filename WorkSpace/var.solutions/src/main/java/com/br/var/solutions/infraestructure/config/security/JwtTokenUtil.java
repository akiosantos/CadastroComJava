package com.br.var.solutions.infraestructure.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements Serializable {
    private static final long serialVersionUID = -25501851562600748L;

    public static final long JWT_TOKEN_EXPIRES = 1800000;

    private String secret = "ea4cd47f1967ffb12e81d23e225b6d5b6821f2d2f70d1e838fdf07624aee6509a9bd4f52846acdd3c19f2c4a7051131f0cb33503a7724888105f89d6d72d373b";

    //retornar o username do token do jwt
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //Retorna vários objetos possíveis de vários tipós possíveis - captura as informações de dentro do token.
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // retorna expiration date do token jwt
    public Date getExpirationDateFromtoken(String token){
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // para retornar qualquer informação do token, e pra isso nós vamos precisar da secret.
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    //validar se o token é expirado.
    private Boolean isTokenExpired(String token){
        final Date expiration = getExpirationDateFromtoken(token);
        return expiration.before(new Date());
    }

    //Gerar Token
    public String generateToken(String clientId){
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, clientId);
    }

    //cria o token e também vai definir o tempo de expiração.
    private String doGenerateToken(Map<String, Object> claims, String clientId) {
        return Jwts.builder().setClaims(claims).setSubject(clientId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_EXPIRES))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    //validar o token
    public Boolean validateToken(String token, UserDetails userDetails){
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}


