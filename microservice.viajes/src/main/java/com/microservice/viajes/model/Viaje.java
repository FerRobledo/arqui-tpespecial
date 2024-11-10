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
    private int tiempo;

    @Column
    private Float monto_viaje;

    @Column
    private String estado;

    @OneToOne
    private Pausa pausa;

    public boolean hasPaused(){
        return pausa != null;
    }
}
