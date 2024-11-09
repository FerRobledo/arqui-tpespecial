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
public class  UsuarioController {

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
    public ResponseEntity<Usuario> getUsuario(@PathVariable ("id") int id) {
        Optional<Usuario> u = usuarioServicio.findById(id);
        if (u.isPresent()) {
            return ResponseEntity.ok(u.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping()
    public ResponseEntity<?> getMonopatinesCercanos() {
        List<?> monopatinesCercanos = usuarioServicio.findMonopatinesCercanos();
        if(!monopatinesCercanos.isEmpty()) {
            return ResponseEntity.ok(monopatinesCercanos);
        } else {
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
        Optional<Usuario> usuarioOptional = usuarioServicio.findById(id);
        if (usuarioOptional.isPresent()) {
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
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUsuario(@PathVariable("id") int id) {
        Optional<Usuario> usuarioOptional = usuarioServicio.findById(id);
        if (usuarioOptional.isPresent()) {
            usuarioServicio.delete(usuarioOptional.get().getId());
            return ResponseEntity.ok("Usuario eliminado con éxito");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
