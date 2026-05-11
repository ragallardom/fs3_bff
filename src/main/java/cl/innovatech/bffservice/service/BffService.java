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

            return Mono.zip(proyectoMono, recursoMono)
                    .map(tuple -> {
                        ProyectoDTO p = tuple.getT1();
                        RecursoDTO r = tuple.getT2();
                        return ProyectoResumenDTO.builder()
                                .nombreProyecto(p.getNombre() != null ? p.getNombre() : "No encontrado")
                                .estadoProyecto(p.getEstado())
                                .totalHoras(p.getTotalHoras())
                                .nombreResponsable(r.getNombre() != null ? r.getNombre() : "Sin asignar")
                                .cargoResponsable(r.getCargo())
                                .build();
                    })
                    .block();
        } catch (Exception e) {
            System.err.println("Error en agregación BFF: " + e.getMessage());
            throw e;
        }
    }
}