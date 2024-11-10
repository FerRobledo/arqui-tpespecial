package com.microservice.viajes.controller;


import com.microservice.viajes.dto.ViajeDTO;
import com.microservice.viajes.model.Viaje;
import com.microservice.viajes.servicios.ViajeServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/viajes")
public class ViajeControlador {

    @Autowired
    private ViajeServicio viajeServicio;

    @PostMapping("/iniciar/user/{id_user}/monopatin/{id_monopatin}")
    @ResponseStatus(HttpStatus.CREATED)
    public void iniciarViaje(@PathVariable int id_user, @PathVariable int id_monopatin) {
        viajeServicio.iniciarViaje(id_user, id_monopatin);
    }

    @GetMapping("")
    public ResponseEntity<List<ViajeDTO>> getViajes() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(viajeServicio.findAll());
        }catch (Exception ex){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/viaje/{id}")
    public ResponseEntity<?> getViaje(@PathVariable int id) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(viajeServicio.findById(id));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/terminar/({id}")
    public ResponseEntity<?> terminarViaje(@PathVariable int id) {
        try{
            Viaje v = viajeServicio.terminarViaje(id);
            return ResponseEntity.status(HttpStatus.OK).body("Viaje terminado.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No se puede terminar viaje");
        }
    }

    @PutMapping("/pausar/{id}")
    public ResponseEntity<?> pausarViaje(@PathVariable int id) {
        try{
            Viaje v = viajeServicio.pausarViaje(id);
            return ResponseEntity.status(HttpStatus.OK).body("Viaje pausado.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se pudo pausar el viaje");
        }
    }

    @GetMapping("/monopatinDTO/{id}")
    public ResponseEntity<?> getMonopatinDTO(@PathVariable int id) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(viajeServicio.findMonopatinById(id));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

