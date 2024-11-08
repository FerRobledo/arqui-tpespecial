package com.microservice.viajes.repository;

import com.microservice.viajes.model.Viaje;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ViajeRepository")
public interface ViajeRepository extends CrudRepository<Viaje, Integer> {

    @Query("SELECT v FROM Viaje v WHERE v.id = :idViaje")
    Viaje findById(int idViaje);

    @Query("SELECT v FROM Viaje v")
    List<Viaje> findAll();
}
