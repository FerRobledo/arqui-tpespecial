package com.microservice.usuario.controller;

import com.microservice.usuario.model.Usuario;
import com.microservice.usuario.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("")
    public ResponseEntity<?> getAllUsuarios() {
        try{
            List<Usuario> usuarios = usuarioServicio.findAll();
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Usuario>> getUsuario(@PathVariable ("id") int id) {
        try{
            Optional<Usuario> u = usuarioServicio.findById(id);
            return ResponseEntity.ok(u);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("")
    public ResponseEntity<?> createUsuario(@RequestBody Usuario usuario) {
        try{
            Usuario u = usuarioServicio.save(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(u);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al cargar los datos del usuario");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable("id") int id, @RequestBody Usuario usuarioDetails) {
        try{
            Optional<Usuario> usuarioOptional = usuarioServicio.findById(id);

            Usuario usuarioExistente = usuarioOptional.get();

            usuarioExistente.setNombre(usuarioDetails.getNombre());
            usuarioExistente.setApellido(usuarioDetails.getApellido());
            usuarioExistente.setNumeroCelular(usuarioDetails.getNumeroCelular());
            usuarioExistente.setEmail(usuarioDetails.getEmail());
            usuarioExistente.setRol(usuarioDetails.getRol());
            usuarioExistente.setFechaAlta(usuarioDetails.getFechaAlta());
            usuarioExistente.setUbicacionUsuario(usuarioDetails.getUbicacionUsuario());

            Usuario usuarioActualizado = usuarioServicio.save(usuarioExistente);

            return ResponseEntity.ok(usuarioActualizado);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUsuario(@PathVariable("id") int id) {
        try{
            Optional<Usuario> usuarioOptional = usuarioServicio.findById(id);
            usuarioServicio.delete(usuarioOptional.get().getId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
