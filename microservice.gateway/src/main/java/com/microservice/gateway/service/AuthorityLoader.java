package com.microservice.gateway.service;

import com.microservice.gateway.entity.Authority;
import com.microservice.gateway.repository.AuthorityRepository;
import com.microservice.gateway.security.AuthotityConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorityLoader implements CommandLineRunner {

    private final AuthorityRepository authorityRepository;

    @Override
    public void run(String... args) throws Exception {
        // Comprobar si las autoridades ya existen en la base de datos
        insertIfNotExists(AuthotityConstant._ADMIN);
        insertIfNotExists(AuthotityConstant._MANTENIMIENTO);
        insertIfNotExists(AuthotityConstant._USUARIO);
    }

    private void insertIfNotExists(String authorityName) {
        // Si la autoridad no existe en la base de datos, crearla
        if (!authorityRepository.existsById(authorityName)) {
            authorityRepository.save(new Authority(authorityName));
        }
    }
}
