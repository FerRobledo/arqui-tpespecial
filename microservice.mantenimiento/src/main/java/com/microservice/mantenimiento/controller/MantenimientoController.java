package com.microservice.mantenimiento.controller;

import com.microservice.mantenimiento.DTO.MantenimientoDTO;
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
    public ResponseEntity<?> addMantenimiento(@RequestBody MantenimientoDTO mantenimientoDTO){
        try{
            Mantenimiento m=mantenimientoServicio.save(mantenimientoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(m);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error al cargar datos del mantenimiento");
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getMantenimientos(){
        try {
            List<MantenimientoDTO> listaMant = mantenimientoServicio.findAll();
            return ResponseEntity.ok(listaMant);
        }catch (Exception ex){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/mantenimiento/{id}")
    public ResponseEntity<?> getMantenimientoById(@PathVariable Long id){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(mantenimientoServicio.findById(id));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editarMantenimiento(@RequestBody MantenimientoDTO dto, @PathVariable ("id") Long id){
        try {
            Mantenimiento m = mantenimientoServicio.editarMantenimiento(dto, id);
            return ResponseEntity.ok(m);
        }catch (Exception ex){
            return ResponseEntity.internalServerError().build();
        }

    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarMantenimiento(@RequestBody MantenimientoDTO mantenimientoDTO) {
        try {
            Mantenimiento mantenimiento = mantenimientoServicio.registrarMantenimiento(mantenimientoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(mantenimiento);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al registrar el mantenimiento");
        }
    }

    @PutMapping("/finalizar/{id}")
    public ResponseEntity<?> finalizarMantenimiento(@PathVariable Long id) {
        try {
            Mantenimiento mantenimiento = mantenimientoServicio.finalizarMantenimiento(id);
            return ResponseEntity.ok(mantenimiento);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mantenimiento no encontrado");
        }
    }
}
