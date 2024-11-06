package com.microservice.viajes.servicios;

import com.microservice.viajes.model.Pausa;
import com.microservice.viajes.repository.PausaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PausaServicio implements Servicios<Pausa> {

    @Autowired
    PausaRepository repo;

    @Override
    public List<Pausa> findAll() {
        return repo.findAll();
    }

    @Override
    public Pausa findById(int id) {
        return repo.findById(id);
    }

    @Override
    public Pausa save(Pausa pausa) {
        return repo.save(pausa);
    }

    @Override
    public void delete(int id) {
        repo.deleteById(id);
    }
}
