package com.microservice.mantenimiento.client;

import com.microservice.mantenimiento.DTO.MonopatinDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name= "microservice-monopatin-paradas", url = "locahost:/8081/api/monopatines")
public interface MantenimientoClient {


    @GetMapping("/{id}")
    MonopatinDTO findMonopatinById(@PathVariable int id);


    @GetMapping("/cambiarEstado/{id}/estado/{estado}")
    void cambiarEstadoMonopatin(@PathVariable int id, String estado);

    @GetMapping("")
    List<MonopatinDTO> getMonopatines();
}
