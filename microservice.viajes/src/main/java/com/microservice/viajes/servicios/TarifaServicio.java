package com.microservice.viajes.servicios;

import com.microservice.viajes.dto.TarifaDTO;
import com.microservice.viajes.model.Tarifa;
import com.microservice.viajes.repository.TarifaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TarifaServicio  {

    @Autowired
    private TarifaRepository repo;


    public List<Tarifa> findAll() {
        return repo.findAll();
    }

    public Tarifa findById(int id) throws Exception{
        Optional<Tarifa> t = repo.findById(id);
        if(t.isPresent()){
            return t.get();
        }
        throw new Exception();
    }

    public Tarifa save(Tarifa tarifa) {
        List<Tarifa> tarifas = repo.findTarifasMismaFecha(tarifa.getFecha_vigencia());
        if(!tarifas.isEmpty()){
            throw new IllegalArgumentException("No se puede añadir una tarifa con fecha vigencia exactamente igual a otra tarifa.");
        }
        return repo.save(tarifa);
    }

    public void delete(int id) {
        repo.deleteById(id);
    }

    public Tarifa ajustarPrecios(TarifaDTO tarifaDTO) {
        List<Tarifa> tarifas= repo.findLatestValidTarifa();
        if (tarifas.isEmpty()) {
            throw new NoSuchElementException("No se encontró ninguna tarifa válida.");
        }
        Tarifa tarifaExistente = tarifas.get(0);

        Float nuevaTarifaNormal = tarifaDTO.getTarifa_normal();
        Float nuevaTarifaAdicional = tarifaDTO.getTarifa_adicional();
        Date fechaVigencia = tarifaDTO.getFecha_vigencia();

        if (tarifaExistente != null && tarifaExistente.getFecha_vigencia().equals(fechaVigencia)) {
            tarifaExistente.setTarifa_normal(nuevaTarifaNormal);
            tarifaExistente.setTarifa_adicional(nuevaTarifaAdicional);
            tarifaExistente.setFecha_vigencia(fechaVigencia);
            return repo.save(tarifaExistente);
        } else {
            Tarifa nuevaTarifa = Tarifa.builder()
                    .tarifa_normal(nuevaTarifaNormal)
                    .tarifa_adicional(nuevaTarifaAdicional)
                    .fecha_vigencia(fechaVigencia)
                    .build();
            return repo.save(nuevaTarifa);
        }
    }
}
