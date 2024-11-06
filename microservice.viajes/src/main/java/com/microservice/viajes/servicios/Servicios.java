package com.microservice.viajes.servicios;

import java.util.List;

public interface Servicios<T> {

    public List<T> findAll();

    public T findById(int id);

    public T save(T t);

    public void delete(int id);
}
