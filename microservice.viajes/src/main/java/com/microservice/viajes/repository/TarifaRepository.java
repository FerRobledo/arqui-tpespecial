package com.microservice.viajes.repository;

import com.microservice.viajes.model.Tarifa;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("TarifaRepository")
public interface TarifaRepository extends CrudRepository<Tarifa, Integer> {

    @Query("SELECT t FROM Tarifa t")
    public List<Tarifa> findAll();

    @Query("SELECT t FROM Tarifa t WHERE t.id = :id")
    public Tarifa findById(int id);
}
