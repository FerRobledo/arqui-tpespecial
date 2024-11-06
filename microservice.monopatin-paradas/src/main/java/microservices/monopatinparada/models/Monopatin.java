package microservices.monopatinparada.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Monopatin {

    @Id
    private Long id;

    @Column
    private int km_recorridos;

    @Column
    private int tiempo_uso;

    @Column
    private String estado;

    @ManyToOne
    private Parada parada;
}
