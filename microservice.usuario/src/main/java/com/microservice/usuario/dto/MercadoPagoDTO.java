package com.microservice.usuario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MercadoPagoDTO {

    private Integer balance;

    private String nombre_cuenta;

    private Set<Integer> usuarios; // Solo IDs de los Usuarios
}
