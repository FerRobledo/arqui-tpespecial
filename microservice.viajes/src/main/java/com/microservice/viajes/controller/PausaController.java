package com.microservice.viajes.controller;

import com.microservice.viajes.servicios.PausaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/pausas")
public class PausaController {

    @Autowired
    private PausaServicio pausaServicio;


}