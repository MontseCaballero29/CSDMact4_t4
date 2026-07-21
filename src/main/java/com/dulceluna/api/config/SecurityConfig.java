package com.dulceluna.api.config;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@Configuration
public class SecurityConfig {

    /*Cifra las contraseñas de los usuarios. */
    @Bean
    PasswordEncoder passwordEncoder() {

        return PasswordEncoderFactories
                .createDelegatingPasswordEncoder();
    }

    /*Convierte el texto secreto en una llave para HS256.*/
    @Bean
    SecretKey jwtSecretKey(
            @Value("${jwt.secret}") String jwtSecret) {

        byte[] clave = jwtSecret.getBytes(StandardCharsets.UTF_8);

        if (clave.length < 32) {
            throw new IllegalArgumentException(
                    "La clave JWT debe tener al menos 32 caracteres");
        }

        return new SecretKeySpec(clave, "HmacSHA256");
    }

    /*Firma y genera los tokens JWT.*/
    @Bean
    JwtEncoder jwtEncoder(SecretKey jwtSecretKey) {

        return NimbusJwtEncoder
                .withSecretKey(jwtSecretKey)
                .algorithm(MacAlgorithm.HS256)
                .build();
    }

    /*Valida la firma y expiración de los tokens recibidos.*/
    @Bean
    JwtDecoder jwtDecoder(SecretKey jwtSecretKey) {

        return NimbusJwtDecoder
                .withSecretKey(jwtSecretKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }
}