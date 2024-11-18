package com.microservice.usuario.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class UserAuthDTO {

    @NotNull( message = "El usuario es un campo requerido." )
    @NotEmpty( message = "El usuario es un campo requerido." )
    private String username;

    @NotNull( message = "La contraseña es un campo requerido." )
    @NotEmpty( message = "La contraseña es un campo requerido." )
    private String password;

    @NotNull( message = "Los roles son un campo requerido." )
    @NotEmpty( message = "Los roles son un campo requerido." )
    private Set<String> authorities;
}
