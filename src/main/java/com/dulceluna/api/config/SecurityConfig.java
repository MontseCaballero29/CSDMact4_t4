package com.dulceluna.api.config;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.dulceluna.api.security.CustomUserDetailsService;
import com.dulceluna.api.security.JwtAuthenticationEntryPoint;

@Configuration
public class SecurityConfig {

    /* Define qué endpoints son públicos y cuáles requieren autenticación JWT.*/
    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint)
            throws Exception {

        http
                /* La API no utiliza formularios ni cookies de sesión.*/
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                /*Cada petición debe autenticarse mediante su JWT.*/
                .sessionManagement(session -> session
                        .sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))

                /*Reglas de acceso.*/
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/error")
                        .permitAll()

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/auth/register",
                                "/api/auth/login")
                        .permitAll()

                        .requestMatchers(
                                "/api/productos/**",
                                "/api/categorias/**")
                        .authenticated()

                        .anyRequest()
                        .denyAll())

                /*Activa la validación de JWT Bearer. */
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults())
                        .authenticationEntryPoint(
                                jwtAuthenticationEntryPoint))

                /*Respuesta JSON cuando no existe, autenticación válida. */
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(
                                jwtAuthenticationEntryPoint));

        return http.build();
    }

    /*Cifra y compara las contraseñas.*/
    @Bean
    PasswordEncoder passwordEncoder() {

        return PasswordEncoderFactories
                .createDelegatingPasswordEncoder();
    }

    /*Busca al usuario en MySQL y compara su contraseña.*/
    @Bean
    DaoAuthenticationProvider authenticationProvider(
            CustomUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    /*Se utiliza durante el endpoint de login.*/
    @Bean
    AuthenticationManager authenticationManager(
            DaoAuthenticationProvider authenticationProvider) {

        return new ProviderManager(authenticationProvider);
    }

    /* Convierte el secreto de texto en una llave HS256. */
    @Bean
    SecretKey jwtSecretKey(
            @Value("${jwt.secret}") String jwtSecret) {

        byte[] clave =
                jwtSecret.getBytes(StandardCharsets.UTF_8);

        if (clave.length < 32) {
            throw new IllegalArgumentException(
                    "La clave JWT debe tener al menos 32 caracteres");
        }

        return new SecretKeySpec(
                clave,
                "HmacSHA256");
    }

    /* Firma los JWT generados por la API.*/
    @Bean
    JwtEncoder jwtEncoder(SecretKey jwtSecretKey) {

        return NimbusJwtEncoder
                .withSecretKey(jwtSecretKey)
                .algorithm(MacAlgorithm.HS256)
                .build();
    }

    /*Valida la firma y expiración de los JWT recibidos.*/
    @Bean
    JwtDecoder jwtDecoder(SecretKey jwtSecretKey) {

        return NimbusJwtDecoder
                .withSecretKey(jwtSecretKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }
}