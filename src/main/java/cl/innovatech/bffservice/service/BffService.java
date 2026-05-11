package cl.innovatech.bffservice.service;

import cl.innovatech.bffservice.dto.ProyectoDTO;
import cl.innovatech.bffservice.dto.ProyectoResumenDTO;
import cl.innovatech.bffservice.dto.RecursoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class BffService {
    private final WebClient webClient;

    @Value("${url.ms.proyectos}")
    private String urlProyectos;

    @Value("${url.ms.recursos}")
    private String urlRecursos;

    public BffService(WebClient webClient) {
        this.webClient = webClient;
    }

    public ProyectoResumenDTO obtenerResumenCompleto(Long proyectoId, Long recursoId) {
        try {
            Mono<ProyectoDTO> proyectoMono = webClient.get()
                    .uri(urlProyectos + "/" + proyectoId + "/detalle")
                    .retrieve()
                    .bodyToMono(ProyectoDTO.class)
                    .onErrorResume(e -> Mono.just(new ProyectoDTO()));

            Mono<RecursoDTO> recursoMono = webClient.get()
                    .uri(urlRecursos + "/" + recursoId)
                    .retrieve()
                    .bodyToMono(RecursoDTO.class)
                    .onErrorResume(e -> Mono.just(new RecursoDTO()));

            Mono<Double> capacidadMono = webClient.get()
                    .uri(urlRecursos + "/" + recursoId + "/capacity")
                    .retrieve()
                    .bodyToMono(Double.class)
                    .onErrorResume(e -> {
                        System.err.println("Error al obtener capacidad MS-RRHH: " + e.getMessage());
                        return Mono.just(0.0);
                    });

            return Mono.zip(proyectoMono, recursoMono, capacidadMono)
                    .map(tuple -> {
                        ProyectoDTO p = tuple.getT1();
                        RecursoDTO r = tuple.getT2();
                        Double cap = tuple.getT3();

                        return ProyectoResumenDTO.builder()
                                .nombreProyecto(p.getNombre() != null ? p.getNombre() : "Proyecto no encontrado")
                                .estadoProyecto(p.getEstado())
                                .totalHoras(p.getTotalHoras())
                                .nombreResponsable(r.getNombre() != null ? r.getNombre() : "Sin asignar")
                                .cargoResponsable(r.getCargo())
                                .capacidadResponsable(cap) // Inyección del dato dinámico
                                .build();
                    })
                    .block();
        } catch (Exception e) {
            System.err.println("Error crítico en la agregación del BFF: " + e.getMessage());
            throw e;
        }
    }
}