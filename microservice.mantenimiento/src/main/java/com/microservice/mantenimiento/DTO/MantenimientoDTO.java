package com.microservice.mantenimiento.DTO;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MantenimientoDTO {

    private int tiempo_uso;

    private int km;

    @NotNull
    private int monopatin_id;

    private String observaciones;

    private Date fecha_mantenimiento;

    @Column(nullable = false)
    private boolean disponible;

}
