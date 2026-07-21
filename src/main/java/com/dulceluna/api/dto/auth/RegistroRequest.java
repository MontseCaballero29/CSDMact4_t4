package com.dulceluna.api.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegistroRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(
        max = 120,
        message = "El nombre no puede superar los 120 caracteres"
    )
    private String nombre;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico no tiene un formato válido")
    @Size(
        max = 150,
        message = "El correo no puede superar los 150 caracteres"
    )
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(
        min = 8,
        max = 100,
        message = "La contraseña debe tener entre 8 y 100 caracteres"
    )
    private String password;

    public RegistroRequest() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}