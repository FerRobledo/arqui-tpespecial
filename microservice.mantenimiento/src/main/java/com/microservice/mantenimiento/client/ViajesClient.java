package com.microservice.mantenimiento.client;

import com.microservice.mantenimiento.DTO.MonopatinDTO;
import com.microservice.mantenimiento.DTO.ViajeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name= "microservice-viajes", url = "locahost:/8084/api/viajes")
public interface ViajesClient {


    @GetMapping("/monopatin/{id}")
    List<ViajeDTO> obtenerViajesPorMonopatinId(@PathVariable ("id") Long id);

    @GetMapping("/viaje/{id}/tiempoPausa")
    int getTiempoPausa(@PathVariable ("id") int id);
}
