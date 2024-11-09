package microservices.monopatinparada.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservices.monopatinparada.models.Monopatin;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParadaDTO {

    private String ubicacion;

    private List<Monopatin> monopatines;

    private int posX;

    private int posY;
}
