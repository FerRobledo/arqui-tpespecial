package com.microservice.viajes.repository;

import com.microservice.viajes.model.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ViajeRepository")
public interface ViajeRepository extends JpaRepository<Viaje, Integer> {

}
