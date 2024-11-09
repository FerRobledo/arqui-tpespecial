package com.microservice.viajes.servicios;

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
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ViajeServicio implements Servicios<Viaje>{

    @Autowired
    private ViajeRepository repo;
    @Autowired
    private TarifaRepository tarifaRepo;
    @Autowired
    private PausaRepository pausaRepo;

    @Override
    public List<Viaje> findAll(){
        return repo.findAll();
    }

    @Override
    public Optional<Viaje> findById(int id){
        return repo.findById(id);
    }

    @Override
    public Viaje save(Viaje viaje){
        return repo.save(viaje);
    }

    @Override
    public void delete(int id){
        repo.deleteById(id);
    }

    public void iniciarViaje(int idUser, int idMonopatin) {
        Viaje v = new Viaje();
        v.setInicio_viaje(new Date());
        v.setEstado("Activo");
        v.setMonopatin_id(idMonopatin);
        v.setUsuario_id(idUser);
        this.repo.save(v);
    }

    public void terminarViaje(int id) throws Exception{
        Optional<Viaje> OptionalV = this.repo.findById(id);
        if(OptionalV.isPresent()){
            Viaje v = OptionalV.get();
            v.setEstado("Finalizado");
            v.setFin_viaje(new Date());
            v.setTiempo(this.calcularTiempo(v.getInicio_viaje(), v.getFin_viaje()));
            v.setMonto_viaje(this.calcularMontoViaje(v));
            this.repo.save(v);
        } else {
            throw new Exception("No se puede terminar el viaje");
        }
    }

    public void pausarViaje(int id) throws Exception{
        Optional<Viaje> OptionalV = this.repo.findById(id);
        if(OptionalV.isPresent()) {
            Viaje v = OptionalV.get();
            v.setEstado("En pausa");
            Pausa p = new Pausa();
            p.setInicio_pausa(new Date());
            p.setViaje(v);
        } else {
            throw new Exception("No se puede pausar el viaje");
        }
    }

    public void finalizarPausa(int id) throws Exception{
        Optional<Viaje> OptionalV = this.repo.findById(id);
        if(OptionalV.isPresent()) {
            Viaje v = OptionalV.get();
            v.setEstado("Activo");
            v.getPausa().setFin_pausa(new Date());
            v.getPausa().setDuracion(calcularTiempo(v.getPausa().getInicio_pausa(), v.getPausa().getFin_pausa()));
            if(v.getPausa().getDuracion() > 15){
                v.setTiempo(v.getTiempo() + v.getPausa().getDuracion() - 15);
            }
        } else {
            throw new Exception("No se pudo finalizar la pausa");
        }
    }

    private int calcularTiempo(Date inicio, Date fin) {
        Instant ini = inicio.toInstant();
        Instant fini = fin.toInstant();
        Duration tiempoViaje = Duration.between(ini, fini);
        return (int) tiempoViaje.toMinutes();
    }

    private float calcularMontoViaje(Viaje v) {
        Tarifa t = tarifaRepo.findLatestValidTarifa();
        float monto;
        if(v.hasPaused()){
            if(v.getPausa().getDuracion() > 15) {
                monto = (v.getTiempo() * t.getTarifa_adicional());
            } else {
                monto = (v.getTiempo() * t.getTarifa_normal());
            }
        }  else {
            monto = (v.getTiempo() * t.getTarifa_normal());
        }

        return monto;
    }
}
