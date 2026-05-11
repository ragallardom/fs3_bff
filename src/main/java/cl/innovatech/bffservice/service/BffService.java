package cl.innovatech.bffservice.service;

import cl.innovatech.bffservice.dto.ProyectoDTO;
import cl.innovatech.bffservice.dto.ProyectoResumenDTO;
import cl.innovatech.bffservice.dto.RecursoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


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

        ProyectoDTO proyecto = webClient.get()
                .uri(urlProyectos + "/" + proyectoId + "/detalle")
                .retrieve()
                .bodyToMono(ProyectoDTO.class)
                .block();

        RecursoDTO recurso = webClient.get()
                .uri(urlRecursos + "/" + recursoId)
                .retrieve()
                .bodyToMono(RecursoDTO.class)
                .block();

        return ProyectoResumenDTO.builder()
                .nombreProyecto(proyecto.getNombre())
                .estadoProyecto(proyecto.getEstado())
                .totalHoras(proyecto.getTotalHoras())
                .nombreResponsable(recurso != null ? recurso.getNombre() : "Sin asignar")
                .cargoResponsable(recurso != null ? recurso.getCargo() : "N/A")
                .build();
    }

}
