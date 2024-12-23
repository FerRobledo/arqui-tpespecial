package microservices.monopatinparada.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservices.monopatinparada.models.Parada;
import org.antlr.v4.runtime.misc.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonopatinDTO {

    private int km_recorridos;

    private int tiempo_uso;

    private String estado;

    private Long paradaID;

    private int posX;

    private int posY;
}
