package com.microservice.viajes.servicios;

import com.microservice.viajes.model.Pausa;
import com.microservice.viajes.repository.PausaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PausaServicio  {

    @Autowired
    PausaRepository repo;

    public List<Pausa> findAll() {
        return repo.findAll();
    }

    public Optional<Pausa> findById(int id) {
        return repo.findById(id);
    }

    public Pausa save(Pausa pausa) {
        return repo.save(pausa);
    }

    public void delete(int id) {
        repo.deleteById(id);
    }
}
