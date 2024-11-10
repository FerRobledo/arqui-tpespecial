package com.microservice.viajes.client;


import com.microservice.viajes.dto.MonopatinDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name= "microservice-monopatin-paradas", url = "locahost:/8081/api/monopatines")
public interface ViajesClient {


    @GetMapping("/{id}")
    MonopatinDTO findMonopatinById(@PathVariable int id);

    @GetMapping("/cambiarEstado/{id}/estado/{estado}")
    void iniciarViajeMonopatin(@PathVariable int id, String estado);

    @GetMapping("moverMonopatin/{id}/x/{x}/y/{y}")
    void moverMonopatin(@PathVariable int id, @PathVariable int x, @PathVariable int y);
}
