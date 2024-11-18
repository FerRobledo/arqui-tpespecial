package com.microservice.viajes.controller;


import com.microservice.viajes.dto.MonopatinViajeDTO;
import com.microservice.viajes.dto.ViajeConID_DTO;
import com.microservice.viajes.dto.ViajeDTO;
import com.microservice.viajes.model.Viaje;
import com.microservice.viajes.servicios.ViajeServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/viajes")
public class ViajeControlador {

    @Autowired
    private ViajeServicio viajeServicio;

    @PostMapping("/iniciar/user/{id_user}/monopatin/{id_monopatin}")
    public ResponseEntity<?> iniciarViaje(@PathVariable int id_user, @PathVariable Long id_monopatin) {
        try{
            Viaje v = viajeServicio.iniciarViaje(id_user, id_monopatin);
            return ResponseEntity.status(HttpStatus.CREATED).body(v);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al iniciar viaje" + e);
        }
    }

    @GetMapping("")
    public ResponseEntity<List<ViajeDTO>> getViajes() {
        try {
            List<ViajeDTO> viajes = viajeServicio.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(viajes);
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

    @PutMapping("/terminar/{id}")
    public ResponseEntity<?> terminarViaje(@PathVariable int id) {
        try {
            Viaje v = viajeServicio.terminarViaje(id);
            return ResponseEntity.status(HttpStatus.OK).body("Viaje terminado con éxito: " + v);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: No se encontró el viaje con ID " + id);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("La fecha de inicio del viaje es null")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: La fecha de inicio del viaje es requerida.");
            } else if (e.getMessage().contains("Las fechas de inicio o fin son null")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Las fechas de inicio o fin del viaje son inválidas.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al terminar el viaje: " + e.getMessage());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado al intentar terminar el viaje.");
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

    @PutMapping("/viaje/{id}/finalizar-pausa")
    public ResponseEntity<?> finalizarPausa(@PathVariable int id) {
        try {
            return ResponseEntity.ok(viajeServicio.finalizarPausa(id));
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }


    @PutMapping("/moverMonopatin/{id}/posX/{x}/posY/{y}")
    public ResponseEntity<?> moverMonopatin(@PathVariable Long id_monopatin, @PathVariable int x, @PathVariable int y) {
        try{
            viajeServicio.moverMonopatin(id_monopatin, x, y);
            return ResponseEntity.status(HttpStatus.OK).body("Ok");
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/monopatin/{id}")
    public ResponseEntity<?> obtenerViajesPorMonopatinId(@PathVariable Long id) {
        try{
            List<ViajeConID_DTO> viajes = viajeServicio.getViajesByMonopatinId(id);
            return ResponseEntity.ok(viajes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/viaje/{id}/tiempoPausa")
    public ResponseEntity<?> getTiempoPausa(@PathVariable int id) {
        try{
            int tiempoPausa = viajeServicio.getTiempoPausa(id);
            return ResponseEntity.ok(tiempoPausa);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/monopatines/anio/{anio}/cantidad/{cantidad}")
    public ResponseEntity<?> findMonopatinesConMasDenXViajesEnAnio(
            @PathVariable ("anio") Integer anio, @PathVariable ("cantidad") Integer cantidad
    ){
        try{
            List<MonopatinViajeDTO> info = viajeServicio.getMonopatinesConMasDeXViajes(anio, cantidad);
            return ResponseEntity.ok(info);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/total-facturado")
    public ResponseEntity<?> obtenerTotalFacturado(@RequestParam int anio,
                                                   @RequestParam int mesInicio,
                                                   @RequestParam int mesFin) {
        try {
            Float totalFacturado = viajeServicio.obtenerTotalFacturado(anio, mesInicio, mesFin);
            return ResponseEntity.status(HttpStatus.OK).body("Total facturado: " + totalFacturado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}

