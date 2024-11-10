package com.microservice.usuario.controller;

import com.microservice.usuario.dto.MonopatinDTO;
import com.microservice.usuario.dto.UsuarioDTO;
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
    public ResponseEntity<List<UsuarioDTO>> getAllUsuarios() {
        List<UsuarioDTO> usuarios = usuarioServicio.findAll();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getUsuario(@PathVariable ("id") int id) {
        Optional<UsuarioDTO> u = usuarioServicio.findById(id);
        if (u.isPresent()) {
            return ResponseEntity.ok(u.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Devuelve true or false para saber si un Usuario es administrador
    @GetMapping("/{id}/isAdmin")
    public ResponseEntity<Boolean> isAdmin(@PathVariable("id") int id) {
        Optional<UsuarioDTO> u = usuarioServicio.findById(id);
        if (u.isPresent()) {
            // Comparar el rol y devolver true o false
            boolean isAdmin = "admin".equalsIgnoreCase(u.get().getRol());
            return ResponseEntity.ok(isAdmin);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/monopatines-cercanos")
    public ResponseEntity<List<MonopatinDTO>> getMonopatinesCercanos(@RequestParam int posX, @RequestParam int posY) {
        List<MonopatinDTO> monopatinesCercanos = usuarioServicio.findMonopatinesCercanos(posX, posY);
        if(!monopatinesCercanos.isEmpty()) {
            return ResponseEntity.ok(monopatinesCercanos);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("")
    public ResponseEntity<?> createUsuario(@RequestBody UsuarioDTO usuario) {
        try{
            UsuarioDTO u = usuarioServicio.save(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(u);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al cargar los datos del usuario");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> updateUsuario(@PathVariable("id") int id, @RequestBody UsuarioDTO usuarioDetails) {
        Optional<UsuarioDTO> usuarioOptional = usuarioServicio.findById(id);
        if (usuarioOptional.isPresent()) {
            UsuarioDTO usuarioExistente = getUsuario(usuarioDetails, usuarioOptional);

            UsuarioDTO usuarioActualizado = usuarioServicio.save(usuarioExistente);
            return ResponseEntity.ok(usuarioActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUsuario(@PathVariable("id") int id) {
        Optional<UsuarioDTO> usuarioOptional = usuarioServicio.findById(id);
        if (usuarioOptional.isPresent()) {
            usuarioServicio.delete(id);
            return ResponseEntity.ok("Usuario eliminado con éxito");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private static UsuarioDTO getUsuario(UsuarioDTO usuarioDetails, Optional<UsuarioDTO> usuarioOptional) {
        UsuarioDTO usuarioExistente = usuarioOptional.get();
        usuarioExistente.setNombre(usuarioDetails.getNombre());
        usuarioExistente.setApellido(usuarioDetails.getApellido());
        usuarioExistente.setNumeroCelular(usuarioDetails.getNumeroCelular());
        usuarioExistente.setEmail(usuarioDetails.getEmail());
        usuarioExistente.setRol(usuarioDetails.getRol());
        usuarioExistente.setPosX(usuarioDetails.getPosX());
        usuarioExistente.setPosY(usuarioDetails.getPosY());
        usuarioExistente.getCuentasMercadoPagoIds().addAll(usuarioDetails.getCuentasMercadoPagoIds());

        return usuarioExistente;
    }

}
