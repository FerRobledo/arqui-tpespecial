package com.microservice.mantenimiento.servicios;

import com.microservice.mantenimiento.DTO.MantenimientoDTO;
import com.microservice.mantenimiento.model.Mantenimiento;
import com.microservice.mantenimiento.repository.MantenimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class MantenimientoServicio {
    @Autowired
    private MantenimientoRepository repository;


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

    public Mantenimiento save(MantenimientoDTO mant){
        Mantenimiento m = this.mapearDTOaMantenimiento(mant);
        return repository.save(m);
    }

    public void delete(Long id) {
        Mantenimiento m = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Mantenimiento con ID " + id + " no encontrado"));
        repository.delete(m);
    }
    public Mantenimiento editarMantenimiento(MantenimientoDTO dto, Long id){
        Mantenimiento m = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Mantenimiento con ID " + id + " no encontrado"));
        m.setTiempo_uso(dto.getTiempo_uso());
        m.setKm(dto.getKm());
        m.setMonopatin_id(dto.getMonopatin_id());
        m.setObservaciones(dto.getObservaciones());
        m.setFecha_mantenimiento(dto.getFecha_mantenimiento());
        return repository.save(m);

    }


    public Mantenimiento mapearDTOaMantenimiento(MantenimientoDTO mantenimientoDTO) {
        Mantenimiento m = new Mantenimiento();
        m.setTiempo_uso(mantenimientoDTO.getTiempo_uso());
        m.setKm(mantenimientoDTO.getKm());
        m.setMonopatin_id(mantenimientoDTO.getMonopatin_id());
        m.setObservaciones(mantenimientoDTO.getObservaciones());
        m.setFecha_mantenimiento(mantenimientoDTO.getFecha_mantenimiento());
        return m;
    }

    public MantenimientoDTO mapearMantenimientoDTO(Mantenimiento mantenimiento) {
        MantenimientoDTO m = new MantenimientoDTO();
        m.setTiempo_uso(mantenimiento.getTiempo_uso());
        m.setKm(mantenimiento.getKm());
        m.setMonopatin_id(mantenimiento.getMonopatin_id());
        m.setObservaciones(mantenimiento.getObservaciones());
        m.setFecha_mantenimiento(mantenimiento.getFecha_mantenimiento());
        m.setDisponible(mantenimiento.isDisponible());
        return m;
    }


    public Mantenimiento registrarMantenimiento(MantenimientoDTO dto) {
        Mantenimiento mantenimiento = mapearDTOaMantenimiento(dto);
        mantenimiento.setDisponible(false);
        return repository.save(mantenimiento);
    }

    public Mantenimiento finalizarMantenimiento(Long id) {
        Mantenimiento mantenimiento = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Mantenimiento no encontrado"));

        mantenimiento.setDisponible(true);
        return repository.save(mantenimiento);
    }

}
