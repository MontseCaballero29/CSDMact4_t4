package com.dulceluna.api.service;

import java.util.Locale;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dulceluna.api.dto.auth.AuthResponse;
import com.dulceluna.api.dto.auth.LoginRequest;
import com.dulceluna.api.dto.auth.RegistroRequest;
import com.dulceluna.api.entity.Rol;
import com.dulceluna.api.entity.Usuario;
import com.dulceluna.api.repository.UsuarioRepository;
import com.dulceluna.api.security.JwtService;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    /*Registra un usuario, cifra su contraseña y genera un JWT. */
    @Transactional
    public AuthResponse registrar(RegistroRequest request) {

        String email = normalizarEmail(request.getEmail());

        if (usuarioRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException(
                    "Ya existe un usuario registrado con ese correo");
        }

        Usuario usuario = new Usuario();

        usuario.setNombre(request.getNombre().trim());
        usuario.setEmail(email);
        usuario.setPassword(
                passwordEncoder.encode(request.getPassword()));
        usuario.setRol(Rol.USER);

        Usuario usuarioGuardado =
                usuarioRepository.save(usuario);

        return generarRespuesta(usuarioGuardado);
    }

    /*Valida el correo y la contraseña mediante Spring Security.*/
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {

        String email = normalizarEmail(request.getEmail());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        request.getPassword()));

        Usuario usuario = usuarioRepository
                .findByEmailIgnoreCase(email)
                .orElseThrow();

        return generarRespuesta(usuario);
    }

    private AuthResponse generarRespuesta(Usuario usuario) {

        String token = jwtService.generarToken(usuario);

        return new AuthResponse(
                token,
                "Bearer",
                jwtService.getExpirationSeconds(),
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol().name());
    }

    private String normalizarEmail(String email) {

        return email
                .trim()
                .toLowerCase(Locale.ROOT);
    }
}