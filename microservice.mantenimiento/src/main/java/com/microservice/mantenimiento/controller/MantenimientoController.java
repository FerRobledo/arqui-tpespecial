package com.microservice.mantenimiento.controller;

import com.microservice.mantenimiento.DTO.MantenimientoDTO;
import com.microservice.mantenimiento.DTO.MonopatinDTO;
import com.microservice.mantenimiento.DTO.ReporteUsoMonopatinDTO;
import com.microservice.mantenimiento.client.MonopatinesClient;
import com.microservice.mantenimiento.model.Mantenimiento;
import com.microservice.mantenimiento.servicios.MantenimientoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/mantenimientos")
public class MantenimientoController {
    @Autowired
    private MantenimientoServicio mantenimientoServicio;
    @Autowired
    private MonopatinesClient monopatinesClient;

    @PostMapping("/registrarMonopatin/{id_monopatin}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addMantenimiento(@PathVariable Long id_monopatin, @RequestBody MantenimientoDTO mantenimientoDTO) {
        try {
            MonopatinDTO monopatin = mantenimientoServicio.findMonopatinById(id_monopatin);
            if (monopatin == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Monopatín no encontrado");
            }
            Mantenimiento mantenimiento = new Mantenimiento();
            mantenimiento.setMonopatin_id(id_monopatin);
            mantenimiento.setObservaciones(mantenimientoDTO.getObservaciones());
            mantenimiento.setFecha_mantenimiento(new Date());
            mantenimiento.setEstado(mantenimientoDTO.getEstado());
            monopatinesClient.cambiarEstadoMonopatin(id_monopatin, "mantenimiento");

            Mantenimiento mantenimientoGuardado = mantenimientoServicio.saveMantenimiento(mantenimiento);

            return ResponseEntity.status(HttpStatus.CREATED).body(mantenimientoGuardado);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error al cargar datos del mantenimiento");
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getMantenimientos(){
        try {
            List<MantenimientoDTO> listaMant = mantenimientoServicio.findAll();
            return ResponseEntity.ok(listaMant);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener los mantenimientos: " + ex.getMessage());
        }
    }


    @GetMapping("/mantenimiento/{id}")
    public ResponseEntity<?> getMantenimientoById(@PathVariable Long id){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(mantenimientoServicio.findById(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mantenimiento no encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud");
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
    public ResponseEntity<?> reporteKilometros(@PathVariable int kilometros, @RequestParam (value = "tiempoPausa") boolean tiempoPausa){
        try{
            List<ReporteUsoMonopatinDTO> reporte = mantenimientoServicio.reporteMonopatinesPorKilometros(kilometros, tiempoPausa);
            return ResponseEntity.ok(reporte);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Monopatines no encontrados");
        }
    }
}
