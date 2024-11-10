package com.microservice.mantenimiento.servicios;

import com.microservice.mantenimiento.DTO.MantenimientoDTO;
import com.microservice.mantenimiento.DTO.MonopatinDTO;
import com.microservice.mantenimiento.client.MantenimientoClient;
import com.microservice.mantenimiento.model.Mantenimiento;
import com.microservice.mantenimiento.repository.MantenimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class MantenimientoServicio {
    @Autowired
    private MantenimientoRepository repository;

    @Autowired
    MantenimientoClient mantenimientoClient;

    public List<MantenimientoDTO> findAll(){
        List<Mantenimiento> lista= repository.findAll();
        List<MantenimientoDTO> listaDTO = new ArrayList<>();

        for(Mantenimiento m:lista){
            MantenimientoDTO dto=this.mapearMantenimientoDTO(m);
            listaDTO.add(dto);
        }
        return listaDTO;
    }

    public MantenimientoDTO findById(Long id){
        Optional<Mantenimiento> m = repository.findById(id);
        if (m.isPresent()) {
            Mantenimiento mant = m.get();
            MantenimientoDTO dto = this.mapearMantenimientoDTO(mant);
            return dto;
        }else{
            throw new NoSuchElementException("Monopatín no encontrado con id: " + id);
        }
    }

    public Mantenimiento save(int id_monopatin){
        MonopatinDTO monopatin = mantenimientoClient.findMonopatinById(id_monopatin);
        if (monopatin != null) {
            Mantenimiento m = new Mantenimiento();
            m.setFecha_mantenimiento(new Date());
            m.setMonopatin_id(id_monopatin);
            m.setEstado("En curso");
            mantenimientoClient.cambiarEstadoMonopatin(id_monopatin, "no disponible");
            return repository.save(m);
        } else {
            return null;
        }

    }

    public void delete(Long id) {
        Mantenimiento m = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Mantenimiento con ID " + id + " no encontrado"));
        repository.delete(m);
    }
    public Mantenimiento editarMantenimiento(MantenimientoDTO dto, Long id){
        Mantenimiento m = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Mantenimiento con ID " + id + " no encontrado"));
        m.setMonopatin_id(dto.getMonopatin_id());
        m.setObservaciones(dto.getObservaciones());
        m.setFecha_mantenimiento(dto.getFecha_mantenimiento());
        return repository.save(m);

    }


    public Mantenimiento mapearDTOaMantenimiento(MantenimientoDTO mantenimientoDTO) {
        Mantenimiento m = new Mantenimiento();
        m.setMonopatin_id(mantenimientoDTO.getMonopatin_id());
        m.setObservaciones(mantenimientoDTO.getObservaciones());
        m.setFecha_mantenimiento(mantenimientoDTO.getFecha_mantenimiento());
        return m;
    }

    public MantenimientoDTO mapearMantenimientoDTO(Mantenimiento mantenimiento) {
        MantenimientoDTO m = new MantenimientoDTO();
        m.setMonopatin_id(mantenimiento.getMonopatin_id());
        m.setObservaciones(mantenimiento.getObservaciones());
        m.setFecha_mantenimiento(mantenimiento.getFecha_mantenimiento());
        return m;
    }

    public Mantenimiento finalizarMantenimiento(Long id) {
        Mantenimiento mantenimiento = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Mantenimiento no encontrado"));
        mantenimiento.setEstado("Finalizado");
        mantenimientoClient.cambiarEstadoMonopatin(mantenimiento.getMonopatin_id(), "disponible");


        return repository.save(mantenimiento);
    }

    public List<MonopatinDTO> reporteMonopatinesPorKilometros(int kilomentros){
        List<MonopatinDTO> monopatines = mantenimientoClient.getMonopatines();
        List<MonopatinDTO> monopatinesFiltrados = new ArrayList<>();

        for(MonopatinDTO m : monopatines) {
            if(m.getKm_recorridos() >= kilomentros){
                monopatinesFiltrados.add(m);
            }
        }

        return monopatinesFiltrados;
    }

}
