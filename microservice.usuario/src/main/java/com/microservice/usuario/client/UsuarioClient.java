package com.microservice.usuario.client;

import com.microservice.usuario.dto.MonopatinDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name= "microservice-monopatin-paradas", url = "locahost:/8081/api/monopatines")
public interface UsuarioClient {

    @GetMapping("/")
    List<MonopatinDTO> getMonopatines();
}
