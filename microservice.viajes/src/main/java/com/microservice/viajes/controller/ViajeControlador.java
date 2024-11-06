package com.microservice.viajes.controller;


import com.microservice.viajes.model.Viaje;
import com.microservice.viajes.servicios.ViajeServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/viaje")
public class ViajeControlador {

    @Autowired
    private ViajeServicio viajeServicio;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveViaje(@RequestBody Viaje viaje) {
        viajeServicio.save(viaje);
    }

    @GetMapping("")
    public ResponseEntity<List<Viaje>> getViajes() {
        return ResponseEntity.status(HttpStatus.OK).body(viajeServicio.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getViaje(@PathVariable int id) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(viajeServicio.findById(id));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }
}

