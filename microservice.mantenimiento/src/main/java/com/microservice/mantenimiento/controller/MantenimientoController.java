package com.microservice.mantenimiento.controller;

import com.microservice.mantenimiento.model.Mantenimiento;
import com.microservice.mantenimiento.servicios.MantenimientoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/mantenimientos")
public class MantenimientoController {
    @Autowired
    private MantenimientoServicio mantenimientoServicio;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveMantenimiento(@RequestBody Mantenimiento mantenimiento){}

    @GetMapping("")
    public ResponseEntity<List<Mantenimiento>> getMantenimientos(){
        return ResponseEntity.status(HttpStatus.OK).body(mantenimientoServicio.findAll());
    }

    @GetMapping("/mantenimiento/{id}")
    public ResponseEntity<?> getMantenimiento(@PathVariable int id){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(mantenimientoServicio.findById(id));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }
}
