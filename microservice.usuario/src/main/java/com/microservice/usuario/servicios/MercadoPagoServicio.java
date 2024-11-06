package com.microservice.usuario.servicios;

import com.microservice.usuario.model.MercadoPago;
import com.microservice.usuario.repository.MercadoPagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MercadoPagoServicio implements Servicios<MercadoPago>{
    private final MercadoPagoRepository mercadoPagoRepository;

    @Autowired
    public MercadoPagoServicio(MercadoPagoRepository mercadoPagoRepository){
        this.mercadoPagoRepository = mercadoPagoRepository;
    }

    @Override
    public List<MercadoPago> findAll() {
        return mercadoPagoRepository.findAll();
    }

    @Override
    public Optional<MercadoPago> findById(int id) {
        return mercadoPagoRepository.findById(id);
    }

    @Override
    public MercadoPago save(MercadoPago mercadoPago) {
        return mercadoPagoRepository.save(mercadoPago);
    }

    @Override
    public void delete(int id) {
        mercadoPagoRepository.deleteById(id);
    }
}
