package com.microservice.viajes.controller;

import com.microservice.viajes.model.Tarifa;
import com.microservice.viajes.servicios.TarifaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tarifa")
public class TarifaController {

    @Autowired
    private TarifaServicio tarifaServicio;

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
}
