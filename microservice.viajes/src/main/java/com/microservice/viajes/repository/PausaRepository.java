package com.microservice.viajes.repository;

import com.microservice.viajes.model.Pausa;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("PausaRepository")
public interface PausaRepository extends CrudRepository<Pausa, Integer> {

    @Query("SELECT p FROM Pausa p")
    List<Pausa> findAll();

    @Query("SELECT p FROM Pausa p WHERE id= :id")
    Pausa findById(int id);
}
