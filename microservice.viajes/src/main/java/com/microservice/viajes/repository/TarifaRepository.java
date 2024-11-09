package com.microservice.viajes.repository;

import com.microservice.viajes.model.Tarifa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("TarifaRepository")
public interface TarifaRepository extends JpaRepository<Tarifa, Integer> {

    @Query("SELECT t FROM Tarifa t")
    public List<Tarifa> findAll();

    @Query("SELECT t FROM Tarifa t WHERE t.fecha_vigencia <= CURRENT_DATE ORDER BY t.fecha_vigencia DESC")
    Tarifa findLatestValidTarifa();
}
