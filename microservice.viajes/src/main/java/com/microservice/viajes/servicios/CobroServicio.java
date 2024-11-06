package com.microservice.viajes.servicios;

import com.microservice.viajes.model.Cobro;
import com.microservice.viajes.repository.CobroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CobroServicio implements Servicios<Cobro>{

    private CobroRepository repo;


    @Override
    public List<Cobro> findAll() {
        return this.repo.findAll();
    }

    @Override
    public Cobro findById(int id){
        return repo.findById(id);
    }

    @Override
    public Cobro save(Cobro cobro){
        return repo.save(cobro);
    }

    @Override
    public void delete(int id){
        repo.deleteById(id);
    }
}
