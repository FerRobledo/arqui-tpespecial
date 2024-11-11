package com.microservice.mantenimiento.DTO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ViajeDTO {

    private int id;

    private Date inicio_viaje;

    private Date fin_viaje;

    private int usuario_id;

    private int monopatin_id;

    private int tiempo;

    private Float monto_viaje;

    private String estado;

    private int pausaID;


}
