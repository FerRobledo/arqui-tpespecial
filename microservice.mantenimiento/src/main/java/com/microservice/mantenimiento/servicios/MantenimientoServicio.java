package com.microservice.mantenimiento.servicios;

import com.microservice.mantenimiento.model.Mantenimiento;
import com.microservice.mantenimiento.repository.MantenimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MantenimientoServicio implements Servicios<Mantenimiento> {
    @Autowired
    private MantenimientoRepository repository;
    @Override
    public List<Mantenimiento> findAll(){
        return repository.findAll();
    }
    @Override
    public Mantenimiento findById(int id){
        return repository.findById(id);
    }
    @Override
    public Mantenimiento save(Mantenimiento mant){
        return repository.save(mant);
    }
    @Override
    public void delete(int id){
        repository.deleteById(id);
    }
}
