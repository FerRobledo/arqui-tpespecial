package com.microservice.usuario.servicios;

import com.microservice.usuario.client.UsuarioClient;
import com.microservice.usuario.dto.MercadoPagoDTO;
import com.microservice.usuario.dto.MonopatinDTO;
import com.microservice.usuario.dto.UsuarioDTO;
import com.microservice.usuario.model.MercadoPago;
import com.microservice.usuario.model.Usuario;
import com.microservice.usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UsuarioServicio {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioClient usuarioClient;

    @Autowired
    private MercadoPagoServicio mercadoPagoServicio;

    public List<UsuarioDTO> findAll() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<UsuarioDTO> usuarioDTOS = new ArrayList<>();

        for (Usuario usuario : usuarios) {
            UsuarioDTO uDTO = this.convertToDTO(usuario);
            usuarioDTOS.add(uDTO);
        }

        return usuarioDTOS;
    }

    public Optional<UsuarioDTO> findById(int id) {
        return usuarioRepository.findById(id).map(this::convertToDTO);
    }

    public UsuarioDTO save(UsuarioDTO usuarioDTO) {
        Usuario usuario = convertToEntity(usuarioDTO);
        Usuario savedUsuario = usuarioRepository.save(usuario);
        return convertToDTO(savedUsuario);
    }

    public void delete(int id){
        usuarioRepository.deleteById(id);
    }

    public List<MonopatinDTO> findMonopatinesCercanos(int usuarioPosX, int usuarioPosY) {
        List<MonopatinDTO> monopatines = usuarioClient.getMonopatines(); // Traigo todos los monopatines
        List<MonopatinDTO> monopatinesCercanos = new ArrayList<>();

        for (MonopatinDTO monopatin : monopatines) {
            if (!"no disponible".equals(monopatin.getEstado())) { // Filtro por estado
                int distancia = calcularDistancia(usuarioPosX, usuarioPosY, monopatin.getPosX(), monopatin.getPosY());
                monopatin.setDistancia(distancia);
                monopatinesCercanos.add(monopatin);
            }
        }

        // Ordeno la lista por la distancia (más cercanos primero)
        monopatinesCercanos.sort(Comparator.comparingInt(MonopatinDTO::getDistancia));

        // Limito la lista a los 10 monopatines más cercanos
        return monopatinesCercanos.stream()
                .limit(10)
                .collect(Collectors.toList());
    }

    // Metodo para calcular la distancia entre 2 puntos
    private int calcularDistancia(int x1, int y1, int x2, int y2) {
        return (int) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    // Metodo de conversión a DTO
    private UsuarioDTO convertToDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setNumeroCelular(usuario.getNumeroCelular());
        dto.setEmail(usuario.getEmail());
        dto.setRol(usuario.getRol());
        dto.setPosX(usuario.getPosX());
        dto.setPosY(usuario.getPosY());

        List<Integer> id_mercadoPago = new ArrayList<>();
        for(MercadoPago mp : usuario.getCuentasMercadoPago()){
            Integer tmp = mp.getId();
            id_mercadoPago.add(tmp);
        }
        dto.setCuentasMercadoPagoIds(id_mercadoPago);


        return dto;
    }

    // Metodo de conversion a Entity (SOLO ES PARA CREATE)
    private Usuario convertToEntity(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setNumeroCelular(dto.getNumeroCelular());
        usuario.setEmail(dto.getEmail());
        usuario.setRol(dto.getRol());
        usuario.setPosX(dto.getPosX());
        usuario.setPosY(dto.getPosY());
        usuario.setFechaAlta(new Date());

        List<MercadoPago> cuentasMp = new ArrayList<>();
        for(Integer idMP : dto.getCuentasMercadoPagoIds()){
            MercadoPagoDTO tmp = mercadoPagoServicio.findById(idMP).orElseThrow(() -> new RuntimeException("Cuenta con el ID: " + idMP + " no existe"));
            MercadoPago mp = mercadoPagoServicio.convertToEntity(tmp);
            cuentasMp.add(mp);
        }
        usuario.setCuentasMercadoPago(cuentasMp);

        return usuario;
    }
}
