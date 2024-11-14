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

    @NotNull
    private Long monopatin_id;

    private String observaciones;

    private Date fecha_mantenimiento;

    private String estado;
}

