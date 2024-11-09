package microservices.monopatinparada.services;

import microservices.monopatinparada.DTO.MonopatinDTO;
import microservices.monopatinparada.DTO.ParadaDTO;
import microservices.monopatinparada.models.Monopatin;
import microservices.monopatinparada.models.Parada;
import microservices.monopatinparada.repositories.MonopatinRepository;
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

    public Monopatin save(MonopatinDTO monopatinDTO){

        Monopatin m = mapearDTOaMonopatin(monopatinDTO);
        return monopatinRepository.save(m);
    }

    public List<MonopatinDTO> getAll(){
        List<Monopatin> lista = monopatinRepository.findAll();
        List<MonopatinDTO> listaDTO = new ArrayList<>();

        for(Monopatin m : lista){
            MonopatinDTO dto = this.mapearMonopatinADTO(m);
            listaDTO.add(dto);
        }

        return listaDTO;
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
        Monopatin m = new Monopatin();
        m.setPosY(monopatinDTO.getPosY());
        m.setPosX(monopatinDTO.getPosX());
        m.setEstado(monopatinDTO.getEstado());
        m.setKm_recorridos(monopatinDTO.getKm_recorridos());
        m.setTiempo_uso(monopatinDTO.getTiempo_uso());
        ParadaDTO paradaDTO = paradaService.getParadaById(monopatinDTO.getParadaID());
        Parada p = paradaService.mapearDTOaParada(paradaDTO);
        m.setParada(p);

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
}
