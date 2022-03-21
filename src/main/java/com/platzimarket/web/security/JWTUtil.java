package com.platzimarket.web.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtil {

    private static final String KEY = "C4r10s";

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername()) //Obtener usuario
                .setIssuedAt(new Date()) //Fecha de Creación del JWT
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) //Definir fecha de expiracion en 10 horas
                .signWith(SignatureAlgorithm.HS256, KEY) //Firma metodo
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        return userDetails.getUsername().equals(extractUsername(token)) && !isTokenExpired(token);
    }

    //Se verifica si el token ya expiró a partir de la fecha actual
    public String extractUsername(String token) {
        return getClaims((token)).getSubject(); //En el getSubject() esta el usuario de la peticion
    }

    public boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    //Los Claims son como los objetos dentro del JWT
    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(KEY) //Se asigna la KEY o llave
                .parseClaimsJws(token).getBody(); //Se Obtiene los objetos del JWT
    }
}
