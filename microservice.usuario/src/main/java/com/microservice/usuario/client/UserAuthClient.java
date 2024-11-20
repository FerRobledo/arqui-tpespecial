package com.microservice.usuario.client;

import com.microservice.usuario.dto.UserAuthDTO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "gateway", url = "http://gateway:8090/api/auth/usuarios", configuration = FeignConfig.class)
public interface UserAuthClient {

    @PostMapping("")
    String saveUser(@RequestBody UserAuthDTO userDTO);

    @GetMapping
    String getPrueba();
}
