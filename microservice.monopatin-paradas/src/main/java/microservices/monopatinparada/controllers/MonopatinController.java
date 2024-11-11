package microservices.monopatinparada.controllers;

import microservices.monopatinparada.DTO.MonopatinConID_DTO;
import microservices.monopatinparada.DTO.MonopatinDTO;
import microservices.monopatinparada.DTO.ParadaDTO;
import microservices.monopatinparada.models.Monopatin;
import microservices.monopatinparada.models.Parada;
import microservices.monopatinparada.services.MonopatinService;
import microservices.monopatinparada.services.ParadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/monopatines")
public class MonopatinController {

    @Autowired
    private MonopatinService monopatinService;

    @Autowired
    private ParadaService paradaService;

    @PostMapping("")
    public ResponseEntity<?> addMonopatin(@RequestBody MonopatinDTO monopatinDTO){
        try {

            Monopatin mAdded = monopatinService.save(monopatinDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(mAdded);
        }catch (Exception ex){
            return ResponseEntity.badRequest().body("Error al cargar datos de monopatín" + ex);
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getMonopatines(){
        try {
            List<MonopatinConID_DTO> mList = monopatinService.getAll();
            return ResponseEntity.ok(mList);
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMonopatinById(@PathVariable ("id") Long id){
        try {
            MonopatinDTO m = monopatinService.getMonopatinById(id);
            return ResponseEntity.ok(m);
        }catch (Exception ex){
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editarMonopatin(@RequestBody MonopatinDTO mDTO, @PathVariable ("id") Long id){
        try {
            Monopatin m = monopatinService.editarMonopatin(mDTO, id);
            return ResponseEntity.ok(m);
        }catch (Exception ex){
            return ResponseEntity.internalServerError().build();
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMonopatinById(@PathVariable ("id") Long id){
        try {
            MonopatinDTO m = monopatinService.deleteMonopatinById(id);
            return ResponseEntity.ok(m);
        }catch (Exception ex){
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/parada/{parada}")
    public ResponseEntity<?> setParadaMonopatin(@PathVariable ("id") Long monopatin_id, @PathVariable ("parada") Long parada_id){
        MonopatinDTO m = monopatinService.getMonopatinById(monopatin_id);
        ParadaDTO p = paradaService.getParadaById(parada_id);

        if(m == null || p == null){
            return ResponseEntity.notFound().build();
        }else{
            try{
                Monopatin monopatin = monopatinService.setParadaMonopatin( p, m);
                return ResponseEntity.ok(monopatin);
            }catch(Exception ex){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al asignar la parada al monopatín");
            }
        }
    }

    @GetMapping("/{id}/paradas/cercanas")
    public ResponseEntity<?> getParadasCercanas(@PathVariable ("id") Long id){
        MonopatinDTO m = monopatinService.getMonopatinById(id);

        if(m == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Monopatín con id: " + id + " no encontrado.");
        }else{
            try {
                List<ParadaDTO> paradasCercanas = paradaService.getParadasOrdenadasPorProximidad(m);
                return ResponseEntity.ok(paradasCercanas);
            }catch (Exception ex){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron paradas cercanas a ese monopatín");
            }
        }
    }

    @PutMapping("/mover/{id}/posX/{posX}/posY/{posY}")
    public ResponseEntity<?> moverMonopatin(
            @PathVariable("id") Long id,
            @PathVariable("posX") int nuevaPosX,
            @PathVariable("posY") int nuevaPosY)
             {
        try {
            if (monopatinService.verificarEstado(id, "en uso")) {
                Monopatin m = monopatinService.moverMonopatin(id, nuevaPosX, nuevaPosY);
                return ResponseEntity.status(HttpStatus.OK)
                        .body("Se movió el monopatín a la posición: X" + nuevaPosX + " Y" + nuevaPosY);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El monopatín no está en uso y no puede ser movido.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al mover monopatín: " + e.getMessage());
        }
    }


    @PutMapping("/cambiarEstado/{id}/estado/{estado}")
    public ResponseEntity<?> cambiarEstado(@PathVariable ("id") Long id, @PathVariable ("estado") String estado){
        try{
            Monopatin m = monopatinService.cambiarEstado(id, estado);
            return ResponseEntity.status(HttpStatus.OK).body("Se cambió el estado del monopatín con éxito" + m);
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al cambiar estado monopatín");
        }
    }

}
