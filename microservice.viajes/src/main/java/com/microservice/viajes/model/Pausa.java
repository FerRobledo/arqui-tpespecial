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
public class Pausa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private Date inicio_pausa;

    @Column
    private Date fin_pausa;

    @OneToOne
    private Viaje viaje;

    @Column
    private int duracion;

}
