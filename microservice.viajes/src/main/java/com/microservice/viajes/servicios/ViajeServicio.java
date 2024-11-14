package com.microservice.viajes.servicios;

import com.microservice.viajes.client.MonopatinesClient;
import com.microservice.viajes.dto.MonopatinDTO;
import com.microservice.viajes.dto.MonopatinViajeDTO;
import com.microservice.viajes.dto.ViajeConID_DTO;
import com.microservice.viajes.dto.ViajeDTO;
import com.microservice.viajes.model.Pausa;
import com.microservice.viajes.model.Tarifa;
import com.microservice.viajes.model.Viaje;
import com.microservice.viajes.repository.PausaRepository;
import com.microservice.viajes.repository.TarifaRepository;
import com.microservice.viajes.repository.ViajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class ViajeServicio {

    @Autowired
    private ViajeRepository repo;
    @Autowired
    private TarifaRepository tarifaRepo;
    @Autowired
    private PausaRepository pausaRepo;
    @Autowired
    private MonopatinesClient viajesClient;

    public List<ViajeDTO> findAll(){
        List<Viaje> viajes =  repo.findAll();
        List<ViajeDTO> viajesDTO = new ArrayList<>();

        for(Viaje v : viajes){
            ViajeDTO tmp = this.mapearViajeADTO(v);
            viajesDTO.add(tmp);
        }

        return viajesDTO;
    }

    public ViajeDTO findById(int id){
        Viaje v = repo.findById(id).orElseThrow(() -> new RuntimeException("No se encontró viaje con el id: " + id));
        return this.mapearViajeADTO(v);
    }

    public Viaje save(ViajeDTO viajeDTO){
       Viaje v = this.mapearDTOaViaje(viajeDTO);
       return repo.save(v);
    }

    public void delete(int id){
        repo.deleteById(id);
    }

    public Viaje iniciarViaje(int idUser, Long idMonopatin) {
        try{
            viajesClient.iniciarViajeMonopatin(idMonopatin, "activo");
            Viaje v = new Viaje();
            v.setInicio_viaje(new Date());
            v.setEstado("activo");
            v.setMonopatin_id(idMonopatin);
            v.setUsuario_id(idUser);
            this.repo.save(v);
            return v;
        } catch (Exception e) {
            throw new RuntimeException("Error en viajeServicio iniciarViaje" + e);
        }
    }

    public Viaje terminarViaje(int id) throws Exception {
        try {
            Viaje v = this.repo.findById(id).orElseThrow(() -> new RuntimeException("Viaje con id " + id + " no existe"));

            if (!v.getEstado().equals("activo")) {
                throw new RuntimeException("El viaje con id " + id + " no está activo y no puede ser finalizado.");
            }
            v.setEstado("finalizado");

            if (v.getInicio_viaje() == null) {
                throw new RuntimeException("La fecha de inicio del viaje es null.");
            }
            if (v.getFin_viaje() == null) {
                v.setFin_viaje(new Date());
            }

            v.setTiempo(this.calcularTiempo(v.getInicio_viaje(), v.getFin_viaje()));

            viajesClient.iniciarViajeMonopatin(v.getMonopatin_id(), "disponible");

            v.setMonto_viaje(this.calcularMontoViaje(v));

            this.repo.save(v);

            return v;
        } catch (Exception e) {
            throw new RuntimeException("Error en viajeServicio terminarViaje: " + e.getMessage(), e);
        }
    }

    public Viaje pausarViaje(int id) throws Exception{
        Viaje v = this.repo.findById(id).orElseThrow(() -> new RuntimeException("Viaje con id " + id + " no existe"));
            v.setEstado("en pausa");
            Pausa p = new Pausa();
            p.setInicio_pausa(new Date());
            p.setViaje(v);
            v.setPausa(p);

            repo.save(v);
            return v;
    }

    public Viaje finalizarPausa(int id) throws Exception{
        Optional<Viaje> OptionalV = this.repo.findById(id);
        if(OptionalV.isPresent()) {
            Viaje v = OptionalV.get();
            v.setEstado("activo");
            v.getPausa().setFin_pausa(new Date());
            v.getPausa().setDuracion(calcularTiempo(v.getPausa().getInicio_pausa(), v.getPausa().getFin_pausa()));
            if(v.getPausa().getDuracion() > 15){
                v.setTiempo(v.getTiempo() + v.getPausa().getDuracion() - 15);
            }
            return v;
        } else {
            throw new Exception("No se pudo finalizar la pausa");
        }
    }

    private int calcularTiempo(Date inicio, Date fin) {
        if (inicio == null || fin == null) {
            throw new RuntimeException("Las fechas de inicio o fin son null.");
        }

        Instant ini = inicio.toInstant();
        Instant fini = fin.toInstant();

        long minutos = ChronoUnit.MINUTES.between(ini, fini);

        return (int) minutos;
    }

    private float calcularMontoViaje(Viaje v) {
        List<Tarifa> tarifas= tarifaRepo.findLatestValidTarifa();
        if (tarifas.isEmpty()) {
            throw new NoSuchElementException("No se encontró ninguna tarifa válida.");
        }
        Tarifa tarifaVigente = tarifas.get(0);

        float monto;
        if (v.getPausa() != null) {
            if (v.getPausa().getDuracion() > 15) {
                monto = (v.getTiempo() * tarifaVigente.getTarifa_adicional());
            } else {
                monto = (v.getTiempo() * tarifaVigente.getTarifa_normal());
            }
        } else {
            monto = (v.getTiempo() * tarifaVigente.getTarifa_normal());
        }

        return monto;
    }

    public MonopatinDTO findMonopatinById(int id) {

        return viajesClient.findMonopatinById(id);

    }


    public Viaje mapearDTOaViaje(ViajeDTO vDTO){
        Viaje v = new Viaje();

        v.setInicio_viaje(vDTO.getInicio_viaje());
        v.setFin_viaje(vDTO.getFin_viaje());
        v.setEstado(vDTO.getEstado());
        v.setTiempo(vDTO.getTiempo());
        v.setMonto_viaje(vDTO.getMonto_viaje());
        v.setUsuario_id(vDTO.getUsuario_id());
        v.setMonopatin_id(vDTO.getMonopatin_id());
        v.setPausa(pausaRepo.findById(vDTO.getPausaID()).orElseThrow(() -> new RuntimeException("No existe pausa con ID: " + vDTO.getPausaID())));

        return v;
    }

    public ViajeDTO mapearViajeADTO(Viaje viaje){
        ViajeDTO vDTO = new ViajeDTO();
        vDTO.setInicio_viaje(viaje.getInicio_viaje());
        vDTO.setFin_viaje(viaje.getFin_viaje());
        vDTO.setEstado(viaje.getEstado());
        vDTO.setTiempo(viaje.getTiempo());
        vDTO.setMonto_viaje(viaje.getMonto_viaje());
        if (viaje.getPausa() != null) {
            vDTO.setPausaID(viaje.getPausa().getId());
        }
        vDTO.setMonopatin_id(viaje.getMonopatin_id());
        vDTO.setUsuario_id(viaje.getUsuario_id());

        return vDTO;
    }

    public ViajeConID_DTO mapearViajeADTOConID(Viaje viaje){
        ViajeConID_DTO vDTO = new ViajeConID_DTO();
        vDTO.setId(viaje.getId());
        vDTO.setInicio_viaje(viaje.getInicio_viaje());
        vDTO.setFin_viaje(viaje.getFin_viaje());
        vDTO.setEstado(viaje.getEstado());
        vDTO.setTiempo(viaje.getTiempo());
        vDTO.setMonto_viaje(viaje.getMonto_viaje());
        if (viaje.getPausa() != null) {
            vDTO.setPausaID(viaje.getPausa().getId());
        }
        vDTO.setMonopatin_id(viaje.getMonopatin_id());
        vDTO.setUsuario_id(viaje.getUsuario_id());

        return vDTO;
    }

    public void moverMonopatin(Long id_monopatin, int x, int y){
        viajesClient.moverMonopatin(id_monopatin, x, y);
    }

    public List<ViajeConID_DTO> getViajesByMonopatinId(Long id){
        List<Viaje> viajes = repo.getViajesByMonopatinId(id);
        List<ViajeConID_DTO> viajesDTO = new ArrayList<>();
        for(Viaje v : viajes){
            ViajeConID_DTO vDto = this.mapearViajeADTOConID(v);
            viajesDTO.add(vDto);
        }

        return viajesDTO;
    }

    public int getTiempoPausa(int id){
        Viaje v = this.repo.findById(id).orElseThrow(() -> new RuntimeException("Viaje con id " + id + " no existe"));

        return v.getPausa().getDuracion();
    }

    public List<MonopatinViajeDTO> getMonopatinesConMasDeXViajes(int anio, long cantidad) {
        return repo.findMonopatinesConMasDenXViajesEnAnio(anio, cantidad);
    }

    public Float obtenerTotalFacturado(int anio, int mesInicio, int mesFin) {
        List<Viaje> viajes = repo.findViajesByAnioAndMeses(anio, mesInicio, mesFin);

        Float totalFacturado = 0f;
        for (Viaje v : viajes) {
            if (v.getMonto_viaje() != null) {
                totalFacturado += v.getMonto_viaje();
            }
        }

        return totalFacturado;
    }
}
