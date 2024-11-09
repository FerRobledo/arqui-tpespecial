package com.microservice.viajes.servicios;

import java.util.List;
import java.util.Optional;

public interface Servicios<T> {

    public List<T> findAll();

    public Optional<T> findById(int id);

    public T save(T t);

    public void delete(int id);
}
