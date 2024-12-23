package com.microservice.mantenimiento.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonopatinDTO {

    private Long id;

    private int km_recorridos;

    private int tiempo_uso;

    private String estado;

    @NotNull
    private Long paradaID;

    private int posX;

    private int posY;
}
