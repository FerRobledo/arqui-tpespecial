package com.microservice.mantenimiento.controller;

import com.microservice.mantenimiento.DTO.MantenimientoDTO;
import com.microservice.mantenimiento.DTO.MonopatinDTO;
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

    @PostMapping("/registrarMonopatin/{id_monopatin}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addMantenimiento(@PathVariable int id_monopatin){
        try{
            Mantenimiento m=mantenimientoServicio.save(id_monopatin);
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

    @PutMapping("/finalizar/{id}")
    public ResponseEntity<?> finalizarMantenimiento(@PathVariable Long id) {
        try {
            Mantenimiento mantenimiento = mantenimientoServicio.finalizarMantenimiento(id);
            return ResponseEntity.ok(mantenimiento);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mantenimiento no encontrado");
        }
    }

    @GetMapping("/reporte/kilometros/{kilometros}")
    public ResponseEntity<?> reporteKilometros(@PathVariable int kilometros){
        try{
            List<MonopatinDTO> monopatines = mantenimientoServicio.reporteMonopatinesPorKilometros(kilometros);
            return ResponseEntity.ok(monopatines);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Monopatines no encontrados");
        }
    }
}
