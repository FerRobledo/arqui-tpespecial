package com.microservice.viajes.servicios;

import com.microservice.viajes.model.Tarifa;
import com.microservice.viajes.repository.TarifaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TarifaServicio implements Servicios<Tarifa>{

    @Autowired
    private TarifaRepository repo;


    @Override
    public List<Tarifa> findAll() {
        return repo.findAll();
    }

    @Override
    public Tarifa findById(int id) {
        return repo.findById(id);
    }

    @Override
    public Tarifa save(Tarifa tarifa) {
        return repo.save(tarifa);
    }

    @Override
    public void delete(int id) {
        repo.deleteById(id);
    }
}
