package com.microservice.viajes.repository;

import com.microservice.viajes.model.Cobro;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("CobroRepository")
public interface CobroRepository extends CrudRepository<Cobro, Integer> {


    @Query("SELECT c FROM Cobro c")
    List<Cobro> findAll();

    @Query("SELECT c FROM Cobro c WHERE c.id = :id")
    Cobro findById(int id);
}
