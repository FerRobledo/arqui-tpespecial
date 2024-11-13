package microservices.monopatinparada.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Monopatin {

    @Id
    @GeneratedValue(strategy  = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int km_recorridos;

    @Column
    private int tiempo_uso;

    @Column
    private String estado;

    @ManyToOne
    @JsonBackReference
    @JsonIgnore
    private Parada parada;

    @Column
    private int posX;

    @Column
    private int posY;
}
