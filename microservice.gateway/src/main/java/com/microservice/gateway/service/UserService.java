package com.microservice.gateway.service;

import com.microservice.gateway.service.dto.user.UserDTO;
import com.microservice.gateway.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import com.microservice.gateway.repository.AuthorityRepository;
import com.microservice.gateway.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;

    public String saveUser(UserDTO request) {
        // Validaciones básicas
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío.");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía.");
        }
        if (request.getAuthorities() == null || request.getAuthorities().isEmpty()) {
            throw new IllegalArgumentException("Debe especificar al menos una autoridad.");
        }

        // Crear entidad User
        final var user = new User(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Buscar roles en la base de datos
        final var roles = authorityRepository.findAllById(request.getAuthorities());
        if (roles.isEmpty()) {
            throw new IllegalArgumentException("Una o más autoridades no existen.");
        }
        user.setAuthorities(roles);

        // Guardar usuario
        final var result = userRepository.save(user);
        return result.getId();
    }
}
