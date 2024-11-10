package com.microservice.mantenimiento.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class Mantenimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int monopatin_id;

    @Column
    private String observaciones;

    @Column
    private Date fecha_mantenimiento;

    @Column
    private String estado;

}