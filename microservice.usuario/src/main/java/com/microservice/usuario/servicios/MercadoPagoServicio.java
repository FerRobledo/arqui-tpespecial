package com.microservice.usuario.servicios;

import com.microservice.usuario.dto.MercadoPagoDTO;
import com.microservice.usuario.model.MercadoPago;
import com.microservice.usuario.model.Usuario;
import com.microservice.usuario.repository.MercadoPagoRepository;
import com.microservice.usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MercadoPagoServicio{

    @Autowired
    private MercadoPagoRepository mercadoPagoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;


    public List<MercadoPagoDTO> findAll() {
        return mercadoPagoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<MercadoPagoDTO> findById(int id) {

        return mercadoPagoRepository.findById(id).map(this::convertToDTO);
    }

    public MercadoPagoDTO save(MercadoPagoDTO mercadoPagoDTO) {
        MercadoPago mercadoPago = convertToEntity(mercadoPagoDTO);
        MercadoPago savedMercadoPago = mercadoPagoRepository.save(mercadoPago);
        return convertToDTO(savedMercadoPago);
    }

    public void delete(int id) {
        mercadoPagoRepository.deleteById(id);
    }

    // Metodo de conversión a DTO
    public MercadoPagoDTO convertToDTO(MercadoPago mp) {
        MercadoPagoDTO dto = new MercadoPagoDTO();
        dto.setBalance(mp.getBalance());
        dto.setNombre_cuenta(mp.getNombre_cuenta());
        dto.setEstado(mp.getEstado());

        if(mp.getUsuarios() != null) {

            List<Integer> usuariosID = new ArrayList<>();
            List<Usuario> usuarios = mp.getUsuarios();
            for(Usuario usuario : usuarios) {
                int id = usuario.getId();
                usuariosID.add(id);
            }
            dto.setUsuarios(usuariosID);

        }


        return dto;
    }

    // Metodo de conversion a Entity (SOLO ES PARA CREATE)
    public MercadoPago convertToEntity(MercadoPagoDTO dto) {
        MercadoPago mp = new MercadoPago();
        mp.setBalance(dto.getBalance());
        mp.setNombre_cuenta(dto.getNombre_cuenta());
        if(dto.getEstado() != null){
            mp.setEstado(dto.getEstado());

            List<Integer> id_usuarios = dto.getUsuarios();

            for(Integer id : id_usuarios){
                Optional<Usuario> usuario = usuarioRepository.findById(id);
                if(usuario.isPresent()){
                    mp.getUsuarios().add(usuario.get());
                }
            }
        }

        return mp;
    }
}
