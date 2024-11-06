package com.microservice.viajes.servicios;

import com.microservice.viajes.model.Viaje;
import com.microservice.viajes.repository.ViajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViajeServicio implements Servicios<Viaje>{

    @Autowired
    private ViajeRepository repo;

    @Override
    public List<Viaje> findAll(){
        return repo.findAll();
    }

    @Override
    public Viaje findById(int id){
        return repo.findById(id);
    }

    @Override
    public Viaje save(Viaje viaje){
        return repo.save(viaje);
    }

    @Override
    public void delete(int id){
        repo.deleteById(id);
    }

}
