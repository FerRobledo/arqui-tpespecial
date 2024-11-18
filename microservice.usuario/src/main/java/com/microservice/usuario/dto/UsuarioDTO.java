package com.microservice.usuario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {

    private String nombre;

    private String apellido;

    private int numeroCelular;

    private String email;

    private int posX;

    private int posY;

    private List<Integer> cuentasMercadoPagoIds; // Solo IDs de las cuentas de MercadoPago

}
