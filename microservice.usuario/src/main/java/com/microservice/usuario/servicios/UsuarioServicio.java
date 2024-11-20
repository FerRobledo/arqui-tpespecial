package com.microservice.usuario.servicios;

import com.microservice.usuario.client.UserAuthClient;
import com.microservice.usuario.client.UsuarioClient;
import com.microservice.usuario.dto.*;
import com.microservice.usuario.model.MercadoPago;
import com.microservice.usuario.model.Usuario;
import com.microservice.usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UsuarioServicio {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioClient usuarioClient;

    @Autowired
    private UserAuthClient userAuthClient;

    @Autowired
    private MercadoPagoServicio mercadoPagoServicio;

    @Transactional(readOnly = true)
    public List<UsuarioDTO> findAll() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<UsuarioDTO> usuarioDTOS = new ArrayList<>();

        for (Usuario usuario : usuarios) {
            UsuarioDTO uDTO = this.convertToDTO(usuario);
            usuarioDTOS.add(uDTO);
        }

        return usuarioDTOS;
    }

    @Transactional(readOnly = true)
    public UsuarioDTO findById(int id) {
        Usuario u = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("No se encontró el usuario con el id: " + id));
        UsuarioDTO uDTO = this.convertToDTO(u);

        return uDTO;
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> findByIdEntity(int id) {
        return usuarioRepository.findById(id);
    }

    public String getPrueba(){
        return userAuthClient.getPrueba();
    }

    public UsuarioDTO save(RegisterUsuarioDTO usuarioDTO) {

        if (usuarioDTO.getUsername() == null || usuarioDTO.getPassword() == null) {
            throw new IllegalArgumentException("El username y password son obligatorios.");
        }
        // Guardamos el usuario en MySQL primero.
        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellido(usuarioDTO.getApellido());
        usuario.setNumeroCelular(usuarioDTO.getNumeroCelular());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setPosX(usuarioDTO.getPosX());
        usuario.setPosY(usuarioDTO.getPosY());
        usuario.setFechaAlta(new Date());
        List<MercadoPago> cuentasMp = new ArrayList<>();
        if(!usuarioDTO.getCuentasMercadoPagoIds().isEmpty()){
            for(Integer idCuenta : usuarioDTO.getCuentasMercadoPagoIds()){
                MercadoPago mp = mercadoPagoServicio.findByIdEntity(idCuenta).orElseThrow(() -> new IllegalArgumentException("No existe la cuenta de mp"));
                cuentasMp.add(mp);
            }
        }
        usuario.setCuentasMercadoPago(cuentasMp);

        // Guardamos el usuario en MySQL y obtenemos su ID.
        Usuario savedUsuario = usuarioRepository.save(usuario);

        // Ahora intentamos guardar el usuario en MongoDB.
        UserAuthDTO userAUTH = new UserAuthDTO();
        userAUTH.setUsername(usuarioDTO.getUsername());
        userAUTH.setPassword(usuarioDTO.getPassword());
        if(!usuarioDTO.getAuthorities().isEmpty()){
        userAUTH.setAuthorities(usuarioDTO.getAuthorities());
        }else{
            throw new IllegalArgumentException("La autoridad es obligatoria.");
        }

        try {
            userAuthClient.saveUser(userAUTH);
        } catch (Exception e) {
            System.err.println("Error al guardar usuario en MongoDB: " + e.getMessage());
            e.printStackTrace(); // Proporciona más contexto al error
            usuarioRepository.delete(savedUsuario);
            throw new RuntimeException("Error al guardar usuario en autenticación", e);
        }

        // Convertimos y devolvemos el DTO del usuario.
        return this.convertToDTO(savedUsuario);
    }


    @Transactional()
    public UsuarioDTO saveExistente(Usuario usuario) {
        Usuario savedUsuario = usuarioRepository.save(usuario);
        return convertToDTO(savedUsuario);
    }

    @Transactional()
    public void delete(int id){
        usuarioRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
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
        usuario.setPosX(dto.getPosX());
        usuario.setPosY(dto.getPosY());
        usuario.setFechaAlta(new Date());

        List<MercadoPago> cuentasMp = new ArrayList<>();
        for (Integer idMP : dto.getCuentasMercadoPagoIds()) {
            Optional<MercadoPago> mpExistente = mercadoPagoServicio.findByIdEntity(idMP);

            if (mpExistente.isPresent()) {
                cuentasMp.add(mpExistente.get());
            }
        }
        usuario.setCuentasMercadoPago(cuentasMp);
        return usuario;
    }
}
