package com.microservice.gateway.service.dto.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;


import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
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
