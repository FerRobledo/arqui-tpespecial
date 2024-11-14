package com.microservice.mantenimiento.servicios;

import com.microservice.mantenimiento.DTO.MantenimientoDTO;
import com.microservice.mantenimiento.DTO.MonopatinDTO;
import com.microservice.mantenimiento.DTO.ReporteUsoMonopatinDTO;
import com.microservice.mantenimiento.DTO.ViajeDTO;
import com.microservice.mantenimiento.client.MonopatinesClient;
import com.microservice.mantenimiento.client.ViajesClient;
import com.microservice.mantenimiento.model.Mantenimiento;
import com.microservice.mantenimiento.repository.MantenimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MantenimientoServicio {
    @Autowired
    private MantenimientoRepository repository;

    @Autowired
    MonopatinesClient monopatinesClient;

    @Autowired
    ViajesClient viajesClient;

    public List<MantenimientoDTO> findAll() {
        List<Mantenimiento> lista = repository.findAll();
        List<MantenimientoDTO> listaDTO = new ArrayList<>();
        for (Mantenimiento m : lista) {
            MantenimientoDTO dto = this.mapearMantenimientoDTO(m);
            listaDTO.add(dto);
        }
        return listaDTO;
    }
    public MantenimientoDTO findById(Long id) {
        Optional<Mantenimiento> m = repository.findById(id);
        if (m.isPresent()) {
            Mantenimiento mant = m.get();
            return this.mapearMantenimientoDTO(mant);
        } else {
            throw new NoSuchElementException("Monopatín no encontrado con id: " + id);
        }
    }
    public Mantenimiento saveMantenimiento(Mantenimiento mantenimiento) {
        return repository.save(mantenimiento);
    }

    public MonopatinDTO findMonopatinById(Long id) {
        return monopatinesClient.findMonopatinById(id);
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
        m.setEstado(dto.getEstado());
        return repository.save(m);

    }


    public Mantenimiento mapearDTOaMantenimiento(MantenimientoDTO mantenimientoDTO) {
        Mantenimiento m = new Mantenimiento();
        m.setMonopatin_id(mantenimientoDTO.getMonopatin_id());
        m.setObservaciones(mantenimientoDTO.getObservaciones());
        m.setFecha_mantenimiento(mantenimientoDTO.getFecha_mantenimiento());
        return m;
    }

    public MantenimientoDTO mapearMantenimientoDTO(Mantenimiento m) {
        MantenimientoDTO dto = new MantenimientoDTO();
        dto.setMonopatin_id(m.getMonopatin_id());
        dto.setObservaciones(m.getObservaciones());
        dto.setFecha_mantenimiento(m.getFecha_mantenimiento());
        dto.setEstado(m.getEstado());
        return dto;
    }


    public Mantenimiento finalizarMantenimiento(Long id) throws Exception {
        Optional<Mantenimiento> mantenimientoOptional = repository.findById(id);

        if (!mantenimientoOptional.isPresent()) {
            throw new Exception("Mantenimiento no encontrado");
        }

        Mantenimiento mantenimiento = mantenimientoOptional.get();
        mantenimiento.setEstado("Finalizado");
        monopatinesClient.cambiarEstadoMonopatin(mantenimiento.getMonopatin_id(), "disponible");
        return repository.save(mantenimiento);
    }


    public List<ReporteUsoMonopatinDTO> reporteMonopatinesPorKilometros(int kilometros, boolean tiempoPausa) {
        List<MonopatinDTO> monopatines = monopatinesClient.getMonopatines();
        List<ReporteUsoMonopatinDTO> reporte = new ArrayList<>();

        for(MonopatinDTO m : monopatines) {
            if(m.getKm_recorridos() >= kilometros){
                ReporteUsoMonopatinDTO tmp = new ReporteUsoMonopatinDTO();
                tmp.setId(m.getId());
                tmp.setKm_recorridos(m.getKm_recorridos());
                tmp.setPosY(m.getPosY());
                tmp.setPosX(m.getPosX());
                tmp.setTiempo_uso(m.getTiempo_uso());
                tmp.setParadaID(m.getParadaID());
                tmp.setEstado(m.getEstado());

                if(tiempoPausa){
                    List<ViajeDTO> viajes = viajesClient.obtenerViajesPorMonopatinId(m.getId());
                    int total = 0;
                    for(ViajeDTO v : viajes){
                        total += viajesClient.getTiempoPausa(v.getId());
                    }
                    tmp.setTiempoPausa(total);
                }
                reporte.add(tmp);
            }
        }
        return reporte;
    }


}
