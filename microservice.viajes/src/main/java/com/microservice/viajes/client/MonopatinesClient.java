package com.microservice.viajes.client;


import com.microservice.viajes.dto.MonopatinDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name= "microservice-monopatin-paradas", url = "locahost:/8081/api/monopatines")
public interface MonopatinesClient {


    @GetMapping("/{id}")
    MonopatinDTO findMonopatinById(@PathVariable int id);

    @GetMapping("/cambiarEstado/{id}/estado/{estado}")
    void iniciarViajeMonopatin(@PathVariable int id, String estado);

    @PutMapping("/mover/{id}/posX/{posX}/posY/{posY}")
    void moverMonopatin(@PathVariable int id, @PathVariable int posX, @PathVariable int posY);
}
