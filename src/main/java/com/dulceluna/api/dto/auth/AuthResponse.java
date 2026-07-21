package com.dulceluna.api.dto.auth;

public class AuthResponse {

    private String token;
    private String tipo;
    private long expiraEn;
    private Long usuarioId;
    private String nombre;
    private String email;
    private String rol;

    public AuthResponse() {
    }

    public AuthResponse(
            String token,
            String tipo,
            long expiraEn,
            Long usuarioId,
            String nombre,
            String email,
            String rol) {

        this.token = token;
        this.tipo = tipo;
        this.expiraEn = expiraEn;
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public long getExpiraEn() {
        return expiraEn;
    }

    public void setExpiraEn(long expiraEn) {
        this.expiraEn = expiraEn;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}