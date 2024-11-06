package com.microservice.usuario.controller;

import com.microservice.usuario.model.MercadoPago;
import com.microservice.usuario.servicios.MercadoPagoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/mercadopago")
public class MercadoPagoController {

    @Autowired
    private MercadoPagoServicio mercadoPagoServicio;

    @GetMapping("")
    public ResponseEntity<?> getAllCuentas(){
        try{
            List<MercadoPago> mercadoPagos = mercadoPagoServicio.findAll();
            return ResponseEntity.ok(mercadoPagos);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<MercadoPago>> getMercadoPago(@PathVariable ("id") int id) {
        try{
            Optional<MercadoPago> mp = mercadoPagoServicio.findById(id);
            return ResponseEntity.ok(mp);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("")
    public ResponseEntity<?> createCuenta(@RequestBody MercadoPago cuenta){
        try{
            MercadoPago mp = mercadoPagoServicio.save(cuenta);
            return ResponseEntity.ok(mp);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al cargar los datos de la cuenta de Mercado Pago");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MercadoPago> updateCuenta(@PathVariable("id") int id, @RequestBody MercadoPago mpDetails) {
        try{
            Optional<MercadoPago> mpOptional = mercadoPagoServicio.findById(id);

            MercadoPago mpExistente = mpOptional.get();

            mpExistente.setNombre_cuenta(mpDetails.getNombre_cuenta());
            mpExistente.setBalance(mpDetails.getBalance());
            mpExistente.setUsuarios(mpDetails.getUsuarios());

            MercadoPago mpActualizado = mercadoPagoServicio.save(mpExistente);

            return ResponseEntity.ok(mpActualizado);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCuenta(@PathVariable("id") int id) {
        try{
            Optional<MercadoPago> mpOptional = mercadoPagoServicio.findById(id);
            mercadoPagoServicio.delete(mpOptional.get().getId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
