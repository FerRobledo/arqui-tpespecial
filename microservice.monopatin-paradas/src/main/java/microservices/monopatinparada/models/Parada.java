package microservices.monopatinparada.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
    private Long id;

    @OneToMany (mappedBy = "parada")
    private List<Monopatin> monopatines;

    @Column
    private int posX;

    @Column
    private int posY;
}
