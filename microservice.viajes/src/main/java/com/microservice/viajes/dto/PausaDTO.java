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
public class PausaDTO{


    private Date inicio_pausa;

    private Date fin_pausa;

    private int viajeID;

    private int duracion;
}
