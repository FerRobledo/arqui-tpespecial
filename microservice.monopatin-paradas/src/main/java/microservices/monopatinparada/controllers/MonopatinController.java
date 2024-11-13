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
import java.util.NoSuchElementException;
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
            return ResponseEntity.ok("Monopatin eliminado con éxito" + m);
        }catch (Exception ex){
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/monopatin/{id}/parada/{id_parada}")
    public ResponseEntity<?> setParadaMonopatin(@PathVariable ("id") Long monopatin_id, @PathVariable ("id_parada") Long parada_id){

        try {
            Monopatin m = monopatinService.setParadaMonopatin(monopatin_id, parada_id);
            return ResponseEntity.ok(m);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al setear la parada");
        }
    }

    @GetMapping("/monopatin/{id}/paradas/cercanas")
    public ResponseEntity<?> getParadasCercanas(@PathVariable ("id") Long id){
        try{
            List<ParadaDTO> paradas = monopatinService.getParadasCercanas(id);
            return ResponseEntity.ok(paradas);
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron paradas para ese ID");
        }
    }

    @PutMapping("/monopatin/{id}/mover/posX/{posX}/posY/{posY}")
    public ResponseEntity<?> moverMonopatin(
            @PathVariable("id") Long id,
            @PathVariable("posX") int nuevaPosX,
            @PathVariable("posY") int nuevaPosY)
             {
        try {
            if (monopatinService.verificarEnUso(id)) {
                MonopatinDTO m = monopatinService.moverMonopatin(id, nuevaPosX, nuevaPosY);
                return ResponseEntity.status(HttpStatus.OK).body("Se movió el monopatín a la posición: X" + nuevaPosX + " Y" + nuevaPosY + m);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El monopatín no está en uso y no puede ser movido.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al mover monopatín: " + e.getMessage());
        }
    }

    @PutMapping("/monopatin/{id}/estado/{estado}")
    public ResponseEntity<?> cambiarEstado(@PathVariable("id") Long id, @PathVariable("estado") String estado) {
        try {
            Monopatin m = monopatinService.cambiarEstado(id, estado);

            return ResponseEntity.status(HttpStatus.OK).body("Se cambió el estado del monopatín con éxito. ID: "
                    + m.getId() + ", Nuevo estado: " + m.getEstado());
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: Monopatín no encontrado con ID: " + id);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al cambiar el estado del monopatín: " + ex.getMessage());
        }
    }

}
