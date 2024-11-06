package com.microservice.viajes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Viaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private Date inicio_viaje;

    @Column
    private Date fin_viaje;

    @Column
    private int usuario_id;

    @Column
    private int monopatin_id;

    @Column
    private int km_recorridos;

    @Column
    private int tiempo;

    @Column
    private int monto_viaje;

    @Column
    private String estado;
}
