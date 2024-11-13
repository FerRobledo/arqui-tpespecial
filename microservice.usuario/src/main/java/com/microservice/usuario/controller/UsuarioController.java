package com.microservice.usuario.controller;

import com.microservice.usuario.dto.MonopatinDTO;
import com.microservice.usuario.dto.UsuarioDTO;
import com.microservice.usuario.model.Usuario;
import com.microservice.usuario.servicios.MercadoPagoServicio;
import com.microservice.usuario.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class  UsuarioController {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private MercadoPagoServicio mpServicio;

    @GetMapping("")
    public ResponseEntity<List<UsuarioDTO>> getAllUsuarios() {
        try{
            List<UsuarioDTO> usuarios = usuarioServicio.findAll();
            return ResponseEntity.ok(usuarios);
        } catch (Exception ex){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getUsuario(@PathVariable ("id") int id) {
        try{
        UsuarioDTO u = usuarioServicio.findById(id);
        return ResponseEntity.ok(u);
        }catch (Exception ex){
            return ResponseEntity.notFound().build();
        }

    }

    //Devuelve true or false para saber si un Usuario es administrador
    @GetMapping("/isAdmin/{id}")
    public ResponseEntity<Boolean> isAdmin(@PathVariable("id") int id) {
         try{
             UsuarioDTO u = usuarioServicio.findById(id);
             boolean isAdmin = "admin".equalsIgnoreCase(u.getRol());
             return ResponseEntity.ok(isAdmin);
         }catch (Exception ex){
             return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
         }

    }

    @GetMapping("/monopatines-cercanos")
    public ResponseEntity<?> getMonopatinesCercanos(@RequestParam int posX, @RequestParam int posY) {
        try {
            List<MonopatinDTO> monopatinesCercanos = usuarioServicio.findMonopatinesCercanos(posX, posY);
            if(!monopatinesCercanos.isEmpty()) {
                return ResponseEntity.ok(monopatinesCercanos);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        } catch (Exception ex) {
            // Log the exception here if needed
            String errorMessage = "Error al obtener los monopatines cercanos: " + ex.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    @PostMapping("")
    public ResponseEntity<?> createUsuario(@RequestBody UsuarioDTO usuario) {
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioServicio.save(usuario));
        } catch (IllegalArgumentException e) {
            // Error de argumentos inválidos (ejemplo: datos no válidos)
            return ResponseEntity.badRequest().body("Error en los datos del usuario: " + e.getMessage());
        } catch (Exception e) {
            // Error genérico del servidor
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> updateUsuario(@PathVariable("id") int id, @RequestBody UsuarioDTO usuarioDetails) {
        Optional<Usuario> usuarioOptional = usuarioServicio.findByIdEntity(id);
        if (usuarioOptional.isPresent()) {
            Usuario usuarioExistente = getUsuario(usuarioDetails, usuarioOptional);
            UsuarioDTO usuarioActualizado = usuarioServicio.saveExistente(usuarioExistente);

            return ResponseEntity.ok(usuarioActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUsuario(@PathVariable("id") int id) {
        try{
            UsuarioDTO usuarioOptional = usuarioServicio.findById(id);
            if(usuarioOptional != null){
                usuarioServicio.delete(id);
            }
            return ResponseEntity.ok("Usuario eliminado con éxito");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    private Usuario getUsuario(UsuarioDTO usuarioDetails, Optional<Usuario> usuarioOptional) {
        Usuario usuarioExistente = usuarioOptional.get();
        usuarioExistente.setNombre(usuarioDetails.getNombre());
        usuarioExistente.setApellido(usuarioDetails.getApellido());
        usuarioExistente.setNumeroCelular(usuarioDetails.getNumeroCelular());
        usuarioExistente.setEmail(usuarioDetails.getEmail());
        usuarioExistente.setRol(usuarioDetails.getRol());
        usuarioExistente.setPosX(usuarioDetails.getPosX());
        usuarioExistente.setPosY(usuarioDetails.getPosY());

        usuarioExistente.getCuentasMercadoPago().clear();

        for (Integer cuentaId : usuarioDetails.getCuentasMercadoPagoIds()) {
            mpServicio.findByIdEntity(cuentaId).ifPresent(usuarioExistente.getCuentasMercadoPago()::add);
        }

        return usuarioExistente;
    }

}
