package microservices.monopatinparada.services;

import microservices.monopatinparada.models.Parada;
import microservices.monopatinparada.repositories.ParadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParadaService {

    @Autowired
    private ParadaRepository paradaRepository;

    public Parada addParada(Parada parada){
        return paradaRepository.save(parada);
    }

    public List<Parada> getAllParadas(){
        return paradaRepository.findAll();
    }

    public Parada getParadaById(Long id){
        return paradaRepository.findParadaById(id);
    }

    public Optional<Parada> deleteParadaById(Long id){
        Optional<Parada> p = paradaRepository.findById(id);

        if(!p.isEmpty()){
            paradaRepository.deleteById(id);
            return p;
        }else{
            return Optional.empty();
        }
    }
}
