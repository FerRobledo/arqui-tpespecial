package com.microservice.usuario.repository;

import com.microservice.usuario.model.MercadoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("MercadoPagoRepository")
public interface MercadoPagoRepository extends JpaRepository<MercadoPago, Integer> {
}
