package microservices.monopatinparada.services;

import jakarta.transaction.Transactional;
import microservices.monopatinparada.DTO.MonopatinConID_DTO;
import microservices.monopatinparada.DTO.MonopatinDTO;
import microservices.monopatinparada.DTO.ParadaDTO;
import microservices.monopatinparada.models.Monopatin;
import microservices.monopatinparada.models.Parada;
import microservices.monopatinparada.repositories.MonopatinRepository;
import microservices.monopatinparada.repositories.ParadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class MonopatinService {

    @Autowired
    private MonopatinRepository monopatinRepository;

    @Autowired
    private ParadaService paradaService;

    @Autowired
    private ParadaRepository paradaRepository;

    public Monopatin save(MonopatinDTO monopatinDTO){

        try{
            Monopatin m = mapearDTOaMonopatin(monopatinDTO);
            return monopatinRepository.save(m);
        } catch (Exception e) {
            throw new RuntimeException("Error al persistir monopatin" + e);
        }
    }

    public List<MonopatinConID_DTO> getAll(){
        try{
            List<Monopatin> lista = monopatinRepository.findAll();
            List<MonopatinConID_DTO> listaDTO = new ArrayList<>();

            for(Monopatin m : lista){
                MonopatinConID_DTO dto = this.mapearMonopatinADTOConID(m);
                listaDTO.add(dto);
            }

            return listaDTO;
        } catch (Exception e) {
            throw new RuntimeException("Error en monopatinServicio getAll" + e);
        }
    }


    public MonopatinDTO getMonopatinById(Long id) {
        Optional<Monopatin> m = monopatinRepository.findById(id);
        if (m.isPresent()) {
            Monopatin mono = m.get();
            MonopatinDTO mDTO = this.mapearMonopatinADTO(mono);
            return mDTO;
        }else{
            throw new NoSuchElementException("Monopatín no encontrado con id: " + id);
        }
    }

    public Monopatin editarMonopatin(MonopatinDTO mDTO, Long id) {
        Monopatin m = monopatinRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Monopatin con ID " + id + " no encontrado"));

        try {
            if (mDTO.getParadaID() != null) {
                Parada p = paradaRepository.findById(mDTO.getParadaID()).orElseThrow(() -> new RuntimeException("Parada no encontrada"));
                m.setParada(p);
            }

            m.setPosY(mDTO.getPosY());
            m.setPosX(mDTO.getPosX());
            m.setEstado(mDTO.getEstado());
            m.setTiempo_uso(mDTO.getTiempo_uso());
            m.setKm_recorridos(mDTO.getKm_recorridos());

             monopatinRepository.save(m);
        }catch (Exception ex){
            throw new RuntimeException("Error al modificar monopatin en servicio");
        }

        return m;
    }

    public MonopatinDTO deleteMonopatinById(Long id) {

        Monopatin m = monopatinRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Monopatin con ID " + id + " no encontrado"));

        monopatinRepository.delete(m);

        return this.mapearMonopatinADTO(m);
    }

    public Monopatin setParadaMonopatin(Long monopatin_id, Long parada_id){
           Monopatin m = monopatinRepository.findById(monopatin_id).orElseThrow(() -> new RuntimeException("Monopatín con id: " + monopatin_id + " no encontrado."));
           Parada p = paradaRepository.findById(parada_id).orElseThrow(() -> new RuntimeException("Parada con id: " + parada_id + " no encontrada."));
       try {

           m.setParada(p);
           m.setPosX(p.getPosX());
           m.setPosY(p.getPosY());

           monopatinRepository.save(m);
       }catch (Exception e){
           throw new RuntimeException("Error al setear parada");
       }
           return m;
    }

    public Monopatin mapearDTOaMonopatin(MonopatinDTO monopatinDTO){
            Monopatin m = new Monopatin();
        try {
            m.setPosY(monopatinDTO.getPosY());
            m.setPosX(monopatinDTO.getPosX());
            m.setEstado(monopatinDTO.getEstado());
            m.setKm_recorridos(monopatinDTO.getKm_recorridos());
            m.setTiempo_uso(monopatinDTO.getTiempo_uso());

            Optional<Parada> parada = paradaRepository.findById(monopatinDTO.getParadaID());
            if (parada.isPresent()) {
                Parada p = parada.get();
                m.setParada(p);
                m.setPosX(p.getPosX());
                m.setPosY(p.getPosY());
            }

        } catch(Exception e){
            throw new RuntimeException("Error al mapear DTO a Monopatin" + e);
        }
            return m;
    }

    public MonopatinDTO mapearMonopatinADTO(Monopatin monopatin){
        MonopatinDTO mDTO = new MonopatinDTO();
        mDTO.setPosY(monopatin.getPosY());
        mDTO.setPosX(monopatin.getPosX());
        mDTO.setEstado(monopatin.getEstado());
        mDTO.setKm_recorridos(monopatin.getKm_recorridos());
        mDTO.setTiempo_uso(monopatin.getTiempo_uso());
        mDTO.setParadaID(monopatin.getParada().getId());

        return mDTO;
    }

    public MonopatinConID_DTO mapearMonopatinADTOConID(Monopatin monopatin){
        MonopatinConID_DTO mDTO = new MonopatinConID_DTO();
        mDTO.setId(monopatin.getId());
        mDTO.setPosY(monopatin.getPosY());
        mDTO.setPosX(monopatin.getPosX());
        mDTO.setEstado(monopatin.getEstado());
        mDTO.setKm_recorridos(monopatin.getKm_recorridos());
        mDTO.setTiempo_uso(monopatin.getTiempo_uso());
        if(monopatin.getParada() != null){
            mDTO.setParadaID(monopatin.getParada().getId());
        }

        return mDTO;
    }

    public MonopatinDTO moverMonopatin(Long id, int nuevaPosX, int nuevaPosY) {
        try {
            Monopatin monopatin = monopatinRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Monopatín no encontrado con ID: " + id));
            MonopatinDTO mDTO = this.mapearMonopatinADTO(monopatin);
            if(monopatin != null){

            int distanciaX = nuevaPosX - monopatin.getPosX();
            int distanciaY = nuevaPosY - monopatin.getPosY();
            double distanciaRecorrida = Math.sqrt(distanciaX * distanciaX + distanciaY * distanciaY);

            monopatin.setPosX(nuevaPosX);
            monopatin.setPosY(nuevaPosY);
            monopatin.setKm_recorridos(monopatin.getKm_recorridos() + (int) distanciaRecorrida);

            mDTO.setPosX(nuevaPosX);
            mDTO.setPosY(nuevaPosY);
            mDTO.setKm_recorridos(monopatin.getKm_recorridos() + (int) distanciaRecorrida);

            monopatinRepository.save(monopatin);
            }
            return mDTO;
        } catch (NoSuchElementException e) {
            throw new RuntimeException("Monopatín no encontrado con ID: " + id, e);
        } catch (Exception e) {
            throw new RuntimeException("Error al mover el monopatín: " + e.getMessage(), e);
        }
    }


    @Transactional
    public Monopatin cambiarEstado(Long id, String estado){
        try{
            Monopatin monopatin = monopatinRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Monopatín no encontrado con ID: " + id));

            monopatin.setEstado(estado);
            monopatinRepository.save(monopatin);
            return monopatin;
        }catch (Exception ex){
            throw new RuntimeException("Error en servicio Monopatin");
        }
    }

    public boolean verificarEnUso(Long id) {
        try {
            Monopatin monopatin = monopatinRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Monopatín no encontrado con ID: " + id));

            if (monopatin.getEstado() == null) {
                throw new IllegalStateException("El monopatín no tiene un estado definido.");
            }
            String estado = "activo";

            return estado.equals(monopatin.getEstado());
        } catch (NoSuchElementException ex) {
            throw new RuntimeException("Monopatín con ID " + id + " no encontrado", ex);
        } catch (IllegalStateException ex) {
            throw new RuntimeException("Estado inválido del monopatín", ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error en servicio monopatin: " + ex.getMessage(), ex);
        }
    }

    public List<ParadaDTO> getParadasCercanas(Long id_monopatin){
        List<ParadaDTO> paradas = new ArrayList<>();
        try{
            Monopatin m = monopatinRepository.findById(id_monopatin).orElseThrow(() -> new RuntimeException("Monopatín no encontrado"));
            MonopatinDTO mDTO = this.mapearMonopatinADTO(m);
            paradas.addAll(paradaService.getParadasOrdenadasPorProximidad(mDTO));
        }catch (Exception ex){
            throw new RuntimeException("Error en servicioMonopatines");
        }
        return paradas;
    }


}
