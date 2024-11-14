package com.microservice.viajes.repository;

import com.microservice.viajes.dto.MonopatinViajeDTO;
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



    @Query("SELECT new com.microservice.viajes.dto.MonopatinViajeDTO(v.monopatin_id, COUNT(v)) " +
            "FROM Viaje v " +
            "WHERE YEAR(v.inicio_viaje) = :anio " +
            "GROUP BY v.monopatin_id " +
            "HAVING COUNT(v) > :cantidad")
    List<MonopatinViajeDTO> findMonopatinesConMasDenXViajesEnAnio(@Param("anio") int anio, @Param("cantidad") long cantidad);

    @Query("SELECT v FROM Viaje v WHERE YEAR(v.inicio_viaje) = :anio " +
            "AND MONTH(v.inicio_viaje) BETWEEN :mesInicio AND :mesFin")
    List<Viaje> findViajesByAnioAndMeses(@Param("anio") int anio,
                                         @Param("mesInicio") int mesInicio,
                                         @Param("mesFin") int mesFin);
}
