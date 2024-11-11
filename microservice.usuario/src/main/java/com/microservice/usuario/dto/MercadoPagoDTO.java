package com.microservice.usuario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MercadoPagoDTO {

    private Integer balance;

    private String nombre_cuenta;

    private String estado;

    private List<Integer> usuarios; // Solo IDs de los Usuarios
}
