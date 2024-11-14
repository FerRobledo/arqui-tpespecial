package microservices.monopatinparada.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CantOperacionMantenimientoDTO {

    private int cantOperacion;

    private int cantMantenimiento;
}
