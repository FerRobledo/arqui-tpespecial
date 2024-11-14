package microservices.monopatinparada.services;

import microservices.monopatinparada.DTO.MonopatinDTO;
import microservices.monopatinparada.DTO.ParadaDTO;
import microservices.monopatinparada.models.Monopatin;
import microservices.monopatinparada.models.Parada;
import microservices.monopatinparada.repositories.ParadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class ParadaService {

    @Autowired
    private ParadaRepository paradaRepository;



    public Parada addParada(ParadaDTO paradaDTO){
        Parada p = this.mapearDTOaParada(paradaDTO);
        return paradaRepository.save(p);
    }

    public List<ParadaDTO> getAllParadas(){
        List<Parada> paradas = paradaRepository.findAll();
        List<ParadaDTO> paradaDTOs = new ArrayList<>();

        for(Parada p : paradas){
            ParadaDTO pDTO = this.mapearParadaADTO(p);
            paradaDTOs.add(pDTO);
        }
        return paradaDTOs;
    }

    public ParadaDTO getParadaById(Long id){
        try{
            Parada p = paradaRepository.findById(id).orElseThrow(() -> new RuntimeException("No se encontro el id de la parada"));

            ParadaDTO pDTO = this.mapearParadaADTO(p);
            return pDTO;
        }catch (Exception e){
            throw new NoSuchElementException("Parada no encontrado con id: " + id + e.getMessage());
        }
    }

    public ParadaDTO deleteParadaById(Long id) {
        Parada p = paradaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Parada con ID " + id + " no encontrada"));
        ParadaDTO pDTO = new ParadaDTO();
        if(!p.getMonopatines().isEmpty()){
            throw new UnsupportedOperationException("No se puede eliminar una parada con monopatines");
        }
        if(p != null){
            pDTO = this.mapearParadaADTO(p);
            paradaRepository.delete(p);
        }

        return pDTO;
    }

    public Parada editarParada(ParadaDTO pDTO, Long id){
        Parada p = paradaRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Parada con ID " + id + " no encontrado"));

        if (!p.getMonopatines().isEmpty()) {
            throw new UnsupportedOperationException("No se puede cambiar una parada que tiene monopatines");
        }

        p.setPosY(pDTO.getPosY());
        p.setPosX(pDTO.getPosX());
        p.setUbicacion(pDTO.getUbicacion());
        if(!pDTO.getMonopatines().isEmpty()){
            p.setMonopatines(pDTO.getMonopatines());
        }

        return paradaRepository.save(p);
    }


    public List<ParadaDTO> getParadasOrdenadasPorProximidad(MonopatinDTO monopatin) {
        try{
            List<Parada> paradas = paradaRepository.findAll();
            List<ParadaDTO> paradasDTO = new ArrayList<>();
            for(Parada p : paradas){
                ParadaDTO pDTO = this.mapearParadaADTO(p);
                paradasDTO.add(pDTO);
            }

            paradasDTO.sort(Comparator.comparingDouble(parada ->
                    calcularDistancia(monopatin.getPosX(), monopatin.getPosY(), parada.getPosX(), parada.getPosY())
            ));

            return paradasDTO;
        }catch (Exception ex){
            throw new RuntimeException("Error en el servicio paradas");
        }
    }

    private double calcularDistancia(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public ParadaDTO mapearParadaADTO(Parada p) {
        ParadaDTO pDTO = new ParadaDTO();
        pDTO.setPosX(p.getPosX());
        pDTO.setPosY(p.getPosY());
        pDTO.setUbicacion(p.getUbicacion());
        if(!p.getMonopatines().isEmpty()){
        pDTO.setMonopatines(p.getMonopatines());
        }

        return pDTO;
    }

    public Parada mapearDTOaParada(ParadaDTO pDTO) {
        Parada p = new Parada();
        p.setPosX(pDTO.getPosX());
        p.setPosY(pDTO.getPosY());
        p.setUbicacion(pDTO.getUbicacion());

        if(pDTO.getMonopatines() != null){
            p.setMonopatines(pDTO.getMonopatines());
        }
        return p;
    }
}
