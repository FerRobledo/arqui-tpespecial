package microservices.monopatinparada.controllers;

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

    //AÑADIR
    @PostMapping("")
    public ResponseEntity<?> addMonopatin(@RequestBody Monopatin monopatin){
        try {
            Monopatin m = monopatinService.save(monopatin);
            return ResponseEntity.status(HttpStatus.CREATED).body(m);
        }catch (Exception ex){
            return ResponseEntity.badRequest().body("Error al cargar datos de monopatín");
        }
    }

    //GETALL
    @GetMapping("")
    public ResponseEntity<?> getMonopatines(){
        try {
            List<Monopatin> mList = monopatinService.getAll();
            return ResponseEntity.ok(mList);
        }catch (Exception ex){
            return ResponseEntity.notFound().build();
        }
    }

    //GETBYID
    @GetMapping("/{id}")
    public ResponseEntity<?> getMonopatinById(@PathVariable ("id") Long id){
        try {
            Monopatin m = monopatinService.getMonopatinById(id);
            return ResponseEntity.ok(m);
        }catch (Exception ex){
            return ResponseEntity.notFound().build();
        }
    }

    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMonopatinById(@PathVariable ("id") Long id){
        try {
            Optional<Monopatin> m = monopatinService.deleteMonopatinById(id);
            return ResponseEntity.ok(m);
        }catch (Exception ex){
            return ResponseEntity.notFound().build();
        }
    }

    //SETPARADA
    @PutMapping("/{id}/{idparada}")
    public ResponseEntity<?> setParadaMonopatin(@PathVariable ("id") Long monopatin_id, @PathVariable ("idparada") Long parada_id){
        Monopatin m = monopatinService.getMonopatinById(monopatin_id);
        Parada p = paradaService.getParadaById(parada_id);

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
}
