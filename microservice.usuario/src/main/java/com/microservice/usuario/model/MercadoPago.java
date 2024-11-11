package com.microservice.usuario.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MercadoPago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Integer balance;

    @Column
    private String nombre_cuenta;

    @Column
    private String estado;

    @ManyToMany(mappedBy = "cuentasMercadoPago")
    private List<Usuario> usuarios;
}
