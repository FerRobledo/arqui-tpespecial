package com.microservice.viajes.controller;

import com.microservice.viajes.dto.TarifaDTO;
import com.microservice.viajes.model.Tarifa;
import com.microservice.viajes.servicios.TarifaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/tarifas")
public class TarifaController {

    @Autowired
    private TarifaServicio tarifaServicio;

    @PostMapping("")
    public ResponseEntity<?> addTarifa(@RequestBody Tarifa tarifa) {
        try {
            Tarifa t = tarifaServicio.save(tarifa);
            return ResponseEntity.ok(t);
        }catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<List<Tarifa>> getTarifas() {
        return  ResponseEntity.status(HttpStatus.OK).body(tarifaServicio.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTarifa(@PathVariable int id) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(tarifaServicio.findById(id));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }

    @PostMapping("/ajustar-precios")
    public ResponseEntity<?> ajustarPrecios(@RequestBody TarifaDTO tarifaDTO)
                                 {
        try {
            return ResponseEntity.ok(tarifaServicio.ajustarPrecios(tarifaDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
