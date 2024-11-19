package com.microservice.usuario.client;

import com.microservice.usuario.dto.UserAuthDTO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name= "microservice.gateway", url = "localhost:8090/api/auth/usuarios")
public interface UserAuthClient {

    @PostMapping
    String saveUser(UserAuthDTO userDTO);
}
