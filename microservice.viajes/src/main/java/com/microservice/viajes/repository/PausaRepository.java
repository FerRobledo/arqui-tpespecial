package com.microservice.viajes.repository;

import com.microservice.viajes.model.Pausa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("PausaRepository")
public interface PausaRepository extends JpaRepository<Pausa, Integer> {

    @Query("SELECT p FROM Pausa p")
    List<Pausa> findAll();
}
