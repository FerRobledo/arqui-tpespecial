package com.microservice.viajes.servicios;

import com.microservice.viajes.model.Tarifa;
import com.microservice.viajes.repository.TarifaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TarifaServicio  {

    @Autowired
    private TarifaRepository repo;


    public List<Tarifa> findAll() {
        return repo.findAll();
    }

    public Tarifa findById(int id) {
        return repo.findById(id);
    }

    public Tarifa save(Tarifa tarifa) {
        return repo.save(tarifa);
    }

    public void delete(int id) {
        repo.deleteById(id);
    }
}
