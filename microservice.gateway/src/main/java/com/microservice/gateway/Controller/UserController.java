package com.microservice.gateway.Controller;

import com.microservice.gateway.service.dto.user.UserDTO;

import lombok.RequiredArgsConstructor;
import com.microservice.gateway.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth/usuarios")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping("")
    public ResponseEntity<?> saveUser(@Validated @RequestBody UserDTO userDTO) {
        try {
            final var id = userService.saveUser(userDTO);
            return new ResponseEntity<>(id, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            System.err.println("Datos inválidos al guardar usuario: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            System.err.println("Error interno al guardar usuario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor", "detalle", e.getMessage()));
        }
    }

    @GetMapping("")
    public String getPrueba(){
        return "Prueba bien";
    }
}
