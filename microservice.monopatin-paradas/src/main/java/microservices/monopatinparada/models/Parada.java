package microservices.monopatinparada.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Parada {

    @Id
    @GeneratedValue(strategy  = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String ubicacion;

    @OneToMany (mappedBy = "parada")
    @JsonManagedReference
    private List<Monopatin> monopatines;

    @Column
    private int posX;

    @Column
    private int posY;
}
