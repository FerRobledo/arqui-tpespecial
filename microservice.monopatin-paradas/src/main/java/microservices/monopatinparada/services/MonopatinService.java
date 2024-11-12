package microservices.monopatinparada.services;

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

    public Monopatin editarMonopatin(MonopatinDTO mDTO, Long id){
        Monopatin m = monopatinRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Monopatin con ID " + id + " no encontrado"));

        Parada p = paradaService.mapearDTOaParada(paradaService.getParadaById(mDTO.getParadaID()));
        m.setParada(p);
        m.setPosX(mDTO.getPosX());
        m.setPosY(mDTO.getPosY());
        m.setEstado(mDTO.getEstado());
        m.setTiempo_uso(mDTO.getTiempo_uso());
        m.setKm_recorridos(mDTO.getKm_recorridos());

        return monopatinRepository.save(m);

    }

    public MonopatinDTO deleteMonopatinById(Long id) {

        Monopatin m = monopatinRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Monopatin con ID " + id + " no encontrado"));

        monopatinRepository.delete(m);

        return this.mapearMonopatinADTO(m);
    }

    public Monopatin setParadaMonopatin(ParadaDTO pDTO, MonopatinDTO mDTO){
        Monopatin mono = this.mapearDTOaMonopatin(mDTO);
        Parada p = paradaService.mapearDTOaParada(pDTO);
        mono.setParada(p);
        mono.setPosY((p.getPosY()));
        mono.setPosX(p.getPosX());
        monopatinRepository.save(mono);

        return mono;

    }

    public Monopatin mapearDTOaMonopatin(MonopatinDTO monopatinDTO){
        try {

            Monopatin m = new Monopatin();
            m.setPosY(monopatinDTO.getPosY());
            m.setPosX(monopatinDTO.getPosX());
            m.setEstado(monopatinDTO.getEstado());
            m.setKm_recorridos(monopatinDTO.getKm_recorridos());
            m.setTiempo_uso(monopatinDTO.getTiempo_uso());

            Optional<Parada> parada = paradaRepository.findById(monopatinDTO.getParadaID());
            if (parada.isPresent()) {
                Parada p = parada.get();
                m.setParada(p);
            }

            return m;
        } catch(Exception e){
            throw new RuntimeException("Error al mapear DTO a Monopatin" + e);

        }
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

    public Monopatin moverMonopatin(Long id, int nuevaPosX, int nuevaPosY) {
        Monopatin monopatin = monopatinRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Monopatín no encontrado con ID: " + id));

        int distanciaX = nuevaPosX - monopatin.getPosX();
        int distanciaY = nuevaPosY - monopatin.getPosY();
        double distanciaRecorrida = Math.sqrt(distanciaX * distanciaX + distanciaY * distanciaY);

        monopatin.setPosX(nuevaPosX);
        monopatin.setPosY(nuevaPosY);
        monopatin.setKm_recorridos(monopatin.getKm_recorridos() + (int) distanciaRecorrida);

        return monopatinRepository.save(monopatin);
    }

    public Monopatin cambiarEstado(Long id, String estado){
        Monopatin monopatin = monopatinRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Monopatín no encontrado con ID: " + id));

        monopatin.setEstado(estado);
        monopatinRepository.save(monopatin);
        return monopatin;
    }

    public boolean verificarEstado(Long id, String estado){
        Monopatin monopatin = monopatinRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Monopatín no encontrado con ID: " + id));

        if(monopatin != null){
            return monopatinRepository.verificarEnUso(id, estado);
        }
        return false;
    }


}
