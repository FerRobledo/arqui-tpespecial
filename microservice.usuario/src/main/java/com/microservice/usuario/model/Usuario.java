package com.microservice.usuario.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String nombre;

    @Column
    private String apellido;

    @Column
    private Integer numeroCelular;

    @Column
    private String email;

    @Column
    private Integer rol;

    @Column
    private Date fechaAlta;

    @Column
    private String ubicacionUsuario;

    @ManyToMany
    @JoinTable(
            name = "Usuario_cuenta",
            joinColumns = @JoinColumn(name = "Usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "MercadoPago_id")
    )
    private Set<MercadoPago> cuentasMercadoPago;
}
