package com.microservice.mantenimiento.repository;

import com.microservice.mantenimiento.model.Mantenimiento;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("MantenimientoRepository")
public interface MantenimientoRepository extends CrudRepository<Mantenimiento, Integer> {

    @Query("SELECT m FROM Mantenimiento m WHERE m.id=:idMantenimiento")
    Mantenimiento findById(int idMantenimiento);

    @Query("SELECT m FROM Mantenimiento m")
    List<Mantenimiento> findAll();
}
