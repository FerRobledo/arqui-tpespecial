package com.microservice.usuario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonopatinDTO {

    private int km_recorridos;

    private int tiempo_uso;

    private String estado;

    private Long paradaID;

    private int posX;

    private int posY;

    private int distancia;
}
