package com.microservice.usuario.servicios;

import com.microservice.usuario.dto.MercadoPagoDTO;
import com.microservice.usuario.model.MercadoPago;
import com.microservice.usuario.model.Usuario;
import com.microservice.usuario.repository.MercadoPagoRepository;
import com.microservice.usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    private MercadoPagoDTO convertToDTO(MercadoPago mp) {
        MercadoPagoDTO dto = new MercadoPagoDTO();
        dto.setBalance(mp.getBalance());
        dto.setNombre_cuenta(mp.getNombre_cuenta());

        // Convierto los Usuarios a un Set de IDs
        Set<Integer> cuentasIds = mp.getUsuarios().stream()
                .map(Usuario::getId)
                .collect(Collectors.toSet());
        dto.setUsuarios(cuentasIds);

        return dto;
    }

    // Metodo de conversion a Entity (SOLO ES PARA CREATE)
    private MercadoPago convertToEntity(MercadoPagoDTO dto) {
        MercadoPago mp = new MercadoPago();
        mp.setBalance(dto.getBalance());
        mp.setNombre_cuenta(dto.getNombre_cuenta());

        // Convierto los IDs de usuarios a entidades Usuario usando el repositorio
        Set<Usuario> usuarios = dto.getUsuarios().stream()
                .map(id -> usuarioRepository.findById(id).orElse(null)) // Busca cada usuario por ID
                .filter(Objects::nonNull) // Filtra aquellos que no existen (si se pasa un ID inválido)
                .collect(Collectors.toSet());

        mp.setUsuarios(usuarios);

        return mp;
    }
}
