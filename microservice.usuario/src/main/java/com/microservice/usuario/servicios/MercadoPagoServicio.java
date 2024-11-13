package com.microservice.usuario.servicios;

import com.microservice.usuario.dto.MercadoPagoDTO;
import com.microservice.usuario.model.MercadoPago;
import com.microservice.usuario.model.Usuario;
import com.microservice.usuario.repository.MercadoPagoRepository;
import com.microservice.usuario.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MercadoPagoServicio{

    @Autowired
    private MercadoPagoRepository mercadoPagoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<MercadoPagoDTO> findAll() {
        return mercadoPagoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<MercadoPagoDTO> findById(int id) {
        return mercadoPagoRepository.findById(id).map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public Optional<MercadoPago> findByIdEntity(int id) {
        return mercadoPagoRepository.findById(id);
    }

    @Transactional()
    public MercadoPagoDTO save(MercadoPagoDTO mercadoPagoDTO) {
        MercadoPago mercadoPago = convertToEntity(mercadoPagoDTO);
        MercadoPago savedMercadoPago = mercadoPagoRepository.save(mercadoPago);
        return convertToDTO(savedMercadoPago);
    }

    @Transactional()
    public MercadoPagoDTO saveExistente(MercadoPago mercadoPago) {
        MercadoPago savedMercadoPago = mercadoPagoRepository.save(mercadoPago);
        return convertToDTO(savedMercadoPago);
    }

    @Transactional()
    public void delete(int id) {
        Optional<MercadoPago> optionalCuenta = mercadoPagoRepository.findById(id);

        if (optionalCuenta.isPresent()) {
            MercadoPago cuenta = optionalCuenta.get();

            // Borro los usuarios relacionados con la cuenta para que no tire error de integridad
            List<Usuario> usuariosRelacionados = cuenta.getUsuarios();
            for (Usuario usuario : usuariosRelacionados) {
                usuario.getCuentasMercadoPago().remove(cuenta);
                usuarioRepository.save(usuario); // Guardo los cambios en cada usuario
            }
            mercadoPagoRepository.deleteById(id);
        }
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

            if (mp.getUsuarios() == null) {
                mp.setUsuarios(new ArrayList<>());
            }

            if(dto.getUsuarios() != null) {
                List<Integer> id_usuarios = dto.getUsuarios();
                for(Integer id : id_usuarios){
                    Optional<Usuario> usuario = usuarioRepository.findById(id);
                    if(usuario.isPresent()) {
                        Usuario user = usuario.get();
                        mp.getUsuarios().add(user);

                        if(user.getCuentasMercadoPago() == null) {
                            user.setCuentasMercadoPago(new ArrayList<>());
                        }
                        user.getCuentasMercadoPago().add(mp);
                    }
                }
            }
        }
        return mp;
    }

}
