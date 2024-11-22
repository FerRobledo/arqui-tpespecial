package org.microservices.monopatinparada.testUnidad;

import microservices.monopatinparada.DTO.MonopatinConID_DTO;
import microservices.monopatinparada.DTO.MonopatinDTO;
import microservices.monopatinparada.models.Monopatin;
import microservices.monopatinparada.models.Parada;
import microservices.monopatinparada.repositories.MonopatinRepository;
import microservices.monopatinparada.repositories.ParadaRepository;
import microservices.monopatinparada.services.MonopatinService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MicroserviceMonopatinParadaApplicationTest {

    @Mock
    private MonopatinRepository monopatinRepository;

    @Mock
    private ParadaRepository paradaRepository;

    @InjectMocks
    private MonopatinService monopatinService;

    @Test
    public void testCrearMonopatin() {
        List<Monopatin> monopatines = new ArrayList<>();
        Parada stop = new Parada(1L, "parada zona centro", monopatines, 5, 5);
        Monopatin esperado = Monopatin.builder()
                .id(1L)
                .km_recorridos(200)
                .tiempo_uso(120)
                .estado("Disponible")
                .parada(stop)
                .posX(5)
                .posY(5)
                .build();

        MonopatinDTO provided = new MonopatinDTO(
                esperado.getKm_recorridos(),
                esperado.getTiempo_uso(),
                esperado.getEstado(),
                esperado.getId(),
                esperado.getPosX(),
                esperado.getPosY()
        );

        Mockito.when(paradaRepository.findById(1L)).thenReturn(Optional.of(stop));

        Mockito.when(monopatinRepository.save(Mockito.any(Monopatin.class))).thenReturn(esperado);

        Monopatin resultado = monopatinService.save(provided);

        Assertions.assertEquals(provided.getKm_recorridos(), resultado.getKm_recorridos());
        Assertions.assertEquals(provided.getTiempo_uso(), resultado.getTiempo_uso());
        Assertions.assertEquals(provided.getEstado(), resultado.getEstado());
        Assertions.assertEquals(provided.getPosX(), resultado.getPosX());
        Assertions.assertEquals(provided.getPosY(), resultado.getPosY());

        Mockito.verify(paradaRepository).findById(1L);
        Mockito.verify(monopatinRepository).save(Mockito.any(Monopatin.class));
    }
    @Test
    public void testGetAll() {
        Parada parada = new Parada(1L, "Parada centro", new ArrayList<>(), 10, 20);
        List<Monopatin> mockMonopatines = new ArrayList<>();
        mockMonopatines.add(new Monopatin(1L, 100, 50, "Disponible", parada, 5, 5));
        mockMonopatines.add(new Monopatin(2L, 200, 120, "En uso", parada, 6, 6));

        Mockito.when(monopatinRepository.findAll()).thenReturn(mockMonopatines);

        List<MonopatinConID_DTO> resultado = monopatinService.getAll();

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(2, resultado.size());
        Assertions.assertEquals("Disponible", resultado.get(0).getEstado());
        Assertions.assertEquals("En uso", resultado.get(1).getEstado());

        Mockito.verify(monopatinRepository).findAll();
    }
}

