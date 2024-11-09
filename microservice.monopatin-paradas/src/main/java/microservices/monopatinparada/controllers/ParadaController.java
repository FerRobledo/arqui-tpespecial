package microservices.monopatinparada.controllers;

import microservices.monopatinparada.DTO.ParadaDTO;
import microservices.monopatinparada.models.Parada;
import microservices.monopatinparada.services.ParadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/paradas")
public class ParadaController {

    @Autowired
    ParadaService paradaService;

    @PostMapping("")
    public ResponseEntity<?> addParada(@RequestBody ParadaDTO parada){
        try{
            Parada p = paradaService.addParada(parada);
            return ResponseEntity.status(HttpStatus.CREATED).body(p);
        } catch(Exception ex){
            return ResponseEntity.badRequest().body("Error al cargar datos de parada");
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAllParadas(){
        try{
            List<ParadaDTO> paradas = paradaService.getAllParadas();
            return ResponseEntity.ok(paradas);
        } catch (Exception ex){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getParadaById(@PathVariable Long id){
        try{
            ParadaDTO p = paradaService.getParadaById(id);
            return ResponseEntity.ok(p);
        } catch (Exception ex){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteParada(@PathVariable Long id){
        try{
            ParadaDTO p = paradaService.deleteParadaById(id);
            return ResponseEntity.ok(p);
        } catch (Exception ex){
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editarParada(@RequestBody ParadaDTO pDTO, @PathVariable ("id") Long id){
        try{
            Parada p = paradaService.editarParada(pDTO, id);
            return ResponseEntity.ok(p);
        }catch(Exception ex){
            return ResponseEntity.internalServerError().build();
        }
    }
}
