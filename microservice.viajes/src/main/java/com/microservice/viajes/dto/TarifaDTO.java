package com.microservice.viajes.dto;


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
public class TarifaDTO {

    private Float tarifa_normal;

    private Float tarifa_adicional;

    private Date fecha_vigencia;

}
