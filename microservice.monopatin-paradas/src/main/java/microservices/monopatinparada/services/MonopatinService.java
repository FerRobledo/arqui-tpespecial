package microservices.monopatinparada.services;

import microservices.monopatinparada.models.Monopatin;
import microservices.monopatinparada.models.Parada;
import microservices.monopatinparada.repositories.MonopatinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MonopatinService {

    @Autowired
    private MonopatinRepository monopatinRepository;

    public Monopatin save(Monopatin monopatin){
        return monopatinRepository.save(monopatin);
    }

    public List<Monopatin> getAll(){
        return monopatinRepository.findAll();
    }


    public Monopatin getMonopatinById(Long id) {
        return monopatinRepository.findMonopatinById(id);
    }

    public Optional<Monopatin> deleteMonopatinById(Long id) {

        Optional<Monopatin> m = monopatinRepository.findById(id);

        if (!m.isEmpty()) {
            deleteMonopatinById(id);
            return m;
        } else {
            return Optional.empty();
        }
    }

    public Monopatin setParadaMonopatin(Parada p, Monopatin m){
        m.setParada(p);
        monopatinRepository.flush();

        return m;

    }
}
