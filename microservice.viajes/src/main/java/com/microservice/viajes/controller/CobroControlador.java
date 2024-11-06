package com.microservice.viajes.controller;

import com.microservice.viajes.model.Cobro;
import com.microservice.viajes.servicios.CobroServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cobro")
public class CobroControlador {

    @Autowired
    CobroServicio cobroServicio;

    @GetMapping("")
    public ResponseEntity<List<Cobro>> getListaCobro() {
        return ResponseEntity.status(HttpStatus.OK).body(cobroServicio.findAll());
    }

}
