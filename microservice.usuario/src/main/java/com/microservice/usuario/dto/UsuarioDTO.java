package com.microservice.usuario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {

    private String nombre;

    private String apellido;

    private int numeroCelular;

    private String email;

    private String rol;

    private int posX;

    private int posY;

    private Set<Integer> cuentasMercadoPagoIds; // Solo IDs de las cuentas de MercadoPago

}
