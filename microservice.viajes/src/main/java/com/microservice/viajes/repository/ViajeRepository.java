package com.microservice.viajes.repository;

import com.microservice.viajes.dto.ViajeDTO;
import com.microservice.viajes.model.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ViajeRepository")
public interface ViajeRepository extends JpaRepository<Viaje, Integer> {

    @Query("SELECT v FROM Viaje v WHERE v.monopatin_id = :id")
    List<Viaje> getViajesByMonopatinId(@Param("id") Long id);
}
