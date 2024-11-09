package microservices.monopatinparada.services;

import microservices.monopatinparada.DTO.MonopatinDTO;
import microservices.monopatinparada.DTO.ParadaDTO;
import microservices.monopatinparada.models.Monopatin;
import microservices.monopatinparada.models.Parada;
import microservices.monopatinparada.repositories.ParadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        Optional<Parada> p = paradaRepository.findById(id);

        if(p.isPresent()){
            Parada parada = p.get();
            ParadaDTO pDTO = this.mapearParadaADTO(parada);
            return pDTO;
        }else{
            throw new NoSuchElementException("Parada no encontrado con id: " + id);
        }
    }

    public ParadaDTO deleteParadaById(Long id){
        Parada p = paradaRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Parada con ID " + id + " no encontrada"));

        paradaRepository.delete(p);

        return this.mapearParadaADTO(p);
    }

    public Parada editarParada(ParadaDTO pDTO, Long id){
        Parada p = paradaRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Parada con ID " + id + " no encontrado"));

        p.setPosY(pDTO.getPosY());
        p.setPosX(pDTO.getPosX());
        p.setUbicacion(pDTO.getUbicacion());
        p.setMonopatines(pDTO.getMonopatines());


        return paradaRepository.save(p);
    }

    public List<ParadaDTO> getParadasOrdenadasPorProximidad(MonopatinDTO monopatin) {
        List<ParadaDTO> paradas = this.getAllParadas();

        paradas.sort(Comparator.comparingDouble(parada ->
                calcularDistancia(monopatin.getPosX(), monopatin.getPosY(), parada.getPosX(), parada.getPosY())
        ));

        return paradas;
    }

    private double calcularDistancia(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public ParadaDTO mapearParadaADTO(Parada p){
        ParadaDTO pDTO = new ParadaDTO();
        pDTO.setPosX(p.getPosX());
        pDTO.setPosY(p.getPosY());
        pDTO.setUbicacion(p.getUbicacion());
        pDTO.setMonopatines(p.getMonopatines());

        return pDTO;
    }

    public Parada mapearDTOaParada(ParadaDTO pDTO){
        Parada p = new Parada();
        p.setPosX(pDTO.getPosX());
        p.setPosY(pDTO.getPosY());
        p.setUbicacion(pDTO.getUbicacion());
        p.setMonopatines(pDTO.getMonopatines());
        return p;
    }
}
