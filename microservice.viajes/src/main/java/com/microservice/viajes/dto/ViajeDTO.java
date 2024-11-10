package com.microservice.viajes.dto;

import com.microservice.viajes.model.Viaje;
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

    private Date inicio_viaje;

    private Date fin_viaje;

    private int usuario_id;

    private int monopatin_id;

    private int km_recorridos;

    private int tiempo;

    private Float monto_viaje;

    private String estado;

    private int pausaID;


}
