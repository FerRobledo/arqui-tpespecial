package com.microservice.usuario.controller;

import com.microservice.usuario.dto.MercadoPagoDTO;
import com.microservice.usuario.model.MercadoPago;
import com.microservice.usuario.servicios.MercadoPagoServicio;
import com.microservice.usuario.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/mercadopago")
public class MercadoPagoController {

    @Autowired
    private MercadoPagoServicio mercadoPagoServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("")
    public ResponseEntity<List<MercadoPagoDTO>> getAllCuentas(){
        List<MercadoPagoDTO> mercadoPagos = mercadoPagoServicio.findAll();
        return ResponseEntity.ok(mercadoPagos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MercadoPagoDTO> getMercadoPago(@PathVariable ("id") int id) {
        Optional<MercadoPagoDTO> mp = mercadoPagoServicio.findById(id);
        if(mp.isPresent()){
            return ResponseEntity.ok(mp.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("")
    public ResponseEntity<?> createCuenta(@RequestBody MercadoPagoDTO cuenta){
        try{
            MercadoPagoDTO mp = mercadoPagoServicio.save(cuenta);
            return ResponseEntity.status(HttpStatus.CREATED).body(mp);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error al cargar los datos de la cuenta de Mercado Pago");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MercadoPagoDTO> updateCuenta(@PathVariable("id") int id, @RequestBody MercadoPagoDTO mpDetails) {
        Optional<MercadoPago> mpOptional = mercadoPagoServicio.findByIdEntity(id);

        if(mpOptional.isPresent()){
            MercadoPago mpExistente = actualizarMercadoPago(mpDetails, mpOptional);
            MercadoPagoDTO mpActualizado = mercadoPagoServicio.saveExistente(mpExistente);

            return ResponseEntity.ok(mpActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/anular/{id}")
    public ResponseEntity<?> anularCuenta(@PathVariable("id") int id, @RequestParam String nuevoEstado){
        Optional<MercadoPago> mpOptional = mercadoPagoServicio.findByIdEntity(id);

        if(mpOptional.isPresent()){
            MercadoPago mpExistente = mpOptional.get();
            mpExistente.setEstado(nuevoEstado);

            MercadoPagoDTO mpActualizado = mercadoPagoServicio.saveExistente(mpExistente);
            return ResponseEntity.ok(mpActualizado);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCuenta(@PathVariable("id") int id) {
        Optional<MercadoPagoDTO> mpOptional = mercadoPagoServicio.findById(id);
        if(mpOptional.isPresent()){
            mercadoPagoServicio.delete(id);
            return ResponseEntity.ok("Cuenta de Mercado Pago eliminada con éxito");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private MercadoPago actualizarMercadoPago(MercadoPagoDTO mpDetails,  Optional<MercadoPago> mpOptional){
        MercadoPago mpExistente = mpOptional.get();
        mpExistente.setNombre_cuenta(mpDetails.getNombre_cuenta());
        mpExistente.setBalance(mpDetails.getBalance());
        mpExistente.setEstado(mpDetails.getEstado());

        mpExistente.getUsuarios().clear();

        for(Integer usuarioId : mpDetails.getUsuarios()){
            usuarioServicio.findByIdEntity(usuarioId).ifPresent(mpExistente.getUsuarios()::add);
        }

        return mpExistente;
    }

}
