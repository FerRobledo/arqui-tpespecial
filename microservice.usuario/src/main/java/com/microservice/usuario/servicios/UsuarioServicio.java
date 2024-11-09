package com.microservice.usuario.servicios;

import com.microservice.usuario.model.Usuario;
import com.microservice.usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServicio implements Servicios<Usuario>{

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioServicio(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<Usuario> findAll(){
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> findById(int id){
        return usuarioRepository.findById(id);
    }

    @Override
    public Usuario save(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    @Override
    public void delete(int id){
        usuarioRepository.deleteById(id);
    }

    public List<?> findMonopatinesCercanos() {return usuarioRepository.findMonopatinesCercanosZona();}
}
