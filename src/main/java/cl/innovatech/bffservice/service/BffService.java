package cl.innovatech.bffservice.service;

import cl.innovatech.bffservice.dto.AsignacionDTO;
import cl.innovatech.bffservice.dto.EmpleadoDTO;
import cl.innovatech.bffservice.dto.NotificacionDTO;
import cl.innovatech.bffservice.dto.ProyectoDTO;
import cl.innovatech.bffservice.dto.ProyectoResumenDTO;
import cl.innovatech.bffservice.dto.RecursoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class BffService {
    private final WebClient webClient;

    @Value("${url.ms.proyectos}")
    private String urlProyectos;

    @Value("${url.ms.recursos}")
    private String urlRecursos;

    @Value("${url.ms.notifications}")
    private String urlNotifications;

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

    public List<EmpleadoDTO> obtenerTodosEmpleados() {
        List<EmpleadoDTO> emps = webClient.get()
                .uri(urlRecursos)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<EmpleadoDTO>>() {})
                .block();

        if (emps != null) {
            reactor.core.publisher.Flux.fromIterable(emps)
                    .flatMap(emp -> webClient.get()
                            .uri(urlRecursos + "/" + emp.getId() + "/capacity")
                            .retrieve()
                            .bodyToMono(Double.class)
                            .map(cap -> {
                                emp.setCapacity(cap);
                                return emp;
                            })
                            .onErrorResume(e -> {
                                emp.setCapacity(0.0);
                                return Mono.just(emp);
                            })
                    )
                    .collectList()
                    .block();
        }
        return emps;
    }

    public EmpleadoDTO obtenerEmpleadoPorId(Long id) {
        return webClient.get()
                .uri(urlRecursos + "/" + id)
                .retrieve()
                .bodyToMono(EmpleadoDTO.class)
                .block();
    }

    public EmpleadoDTO crearEmpleado(EmpleadoDTO empleado) {
        return webClient.post()
                .uri(urlRecursos)
                .bodyValue(empleado)
                .retrieve()
                .bodyToMono(EmpleadoDTO.class)
                .block();
    }

    public EmpleadoDTO actualizarEmpleado(Long id, EmpleadoDTO empleado) {
        return webClient.put()
                .uri(urlRecursos + "/" + id)
                .bodyValue(empleado)
                .retrieve()
                .bodyToMono(EmpleadoDTO.class)
                .block();
    }

    public void eliminarEmpleado(Long id) {
        webClient.delete()
                .uri(urlRecursos + "/" + id)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public Double obtenerCapacidadEmpleado(Long id) {
        return webClient.get()
                .uri(urlRecursos + "/" + id + "/capacity")
                .retrieve()
                .bodyToMono(Double.class)
                .block();
    }

    public List<ProyectoDTO> obtenerTodosProyectos() {
        return webClient.get()
                .uri(urlProyectos)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ProyectoDTO>>() {})
                .block();
    }

    public ProyectoDTO obtenerProyectoPorId(Long id) {
        return webClient.get()
                .uri(urlProyectos + "/" + id + "/detalle")
                .retrieve()
                .bodyToMono(ProyectoDTO.class)
                .block();
    }

    public ProyectoDTO crearProyecto(ProyectoDTO proyecto) {
        return webClient.post()
                .uri(urlProyectos)
                .bodyValue(proyecto)
                .retrieve()
                .bodyToMono(ProyectoDTO.class)
                .block();
    }

    public ProyectoDTO actualizarProyecto(Long id, ProyectoDTO proyecto) {
        return webClient.put()
                .uri(urlProyectos + "/" + id)
                .bodyValue(proyecto)
                .retrieve()
                .bodyToMono(ProyectoDTO.class)
                .block();
    }

    public void eliminarProyecto(Long id) {
        webClient.delete()
                .uri(urlProyectos + "/" + id)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public NotificacionDTO crearNotificacion(NotificacionDTO notificacion) {
        return webClient.post()
                .uri(urlNotifications)
                .bodyValue(notificacion)
                .retrieve()
                .bodyToMono(NotificacionDTO.class)
                .block();
    }

    public NotificacionDTO marcarNotificacionComoLeida(Long id) {
        return webClient.put()
                .uri(urlNotifications + "/" + id + "/leer")
                .retrieve()
                .bodyToMono(NotificacionDTO.class)
                .block();
    }

    public List<NotificacionDTO> obtenerNotificacionesCarga() {
        // 1. Fetch existing notifications
        List<NotificacionDTO> notificacionesExistentes = webClient.get()
                .uri(urlNotifications)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<NotificacionDTO>>() {})
                .block();

        // 2. Fetch all employees (which computes capacity)
        List<EmpleadoDTO> empleados = obtenerTodosEmpleados();

        // 3. Analyze workload and auto-generate new alert notifications
        if (empleados != null) {
            for (EmpleadoDTO emp : empleados) {
                // High Workload: capacity <= 10.0 and horasAsignadas > 0
                if (emp.getCapacity() != null && emp.getCapacity() <= 10.0 && emp.getHorasAsignadas() > 0) {
                    String titulo = "Sobrecarga: " + emp.getNombre();
                    String mensaje = "El empleado " + emp.getNombre() + " (" + emp.getCargo() + ") tiene una disponibilidad crítica de " + emp.getCapacity() + " horas.";
                    
                    boolean existe = notificacionesExistentes != null && notificacionesExistentes.stream()
                            .anyMatch(n -> n.getTitulo().equals(titulo) && !Boolean.TRUE.equals(n.getLeida()));
                    
                    if (!existe) {
                        NotificacionDTO nueva = NotificacionDTO.builder()
                                .titulo(titulo)
                                .mensaje(mensaje)
                                .tipo("ALERTA")
                                .leida(false)
                                .build();
                        try {
                            crearNotificacion(nueva);
                        } catch (Exception e) {
                            System.err.println("Error al crear notificacion: " + e.getMessage());
                        }
                    }
                }

                // Low Workload: capacity >= 35.0 (for UX/Specialist) or assigned hours <= 5
                if (emp.getCapacity() != null && (emp.getCapacity() >= 35.0 || emp.getHorasAsignadas() <= 5)) {
                    String titulo = "Baja Carga: " + emp.getNombre();
                    String mensaje = "El empleado " + emp.getNombre() + " (" + emp.getCargo() + ") tiene baja asignación de trabajo (" + emp.getHorasAsignadas() + "h asignadas, " + emp.getCapacity() + "h disponibles).";
                    
                    boolean existe = notificacionesExistentes != null && notificacionesExistentes.stream()
                            .anyMatch(n -> n.getTitulo().equals(titulo) && !Boolean.TRUE.equals(n.getLeida()));
                    
                    if (!existe) {
                        NotificacionDTO nueva = NotificacionDTO.builder()
                                .titulo(titulo)
                                .mensaje(mensaje)
                                .tipo("INFO")
                                .leida(false)
                                .build();
                        try {
                            crearNotificacion(nueva);
                        } catch (Exception e) {
                            System.err.println("Error al crear notificacion: " + e.getMessage());
                        }
                    }
                }
            }
        }

        // 4. Return refreshed list
        return webClient.get()
                .uri(urlNotifications)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<NotificacionDTO>>() {})
                .block();
    }

    public List<AsignacionDTO> obtenerAsignacionesPorProyecto(Long proyectoId) {
        List<AsignacionDTO> raw = webClient.get()
                .uri(urlProyectos + "/asignaciones/proyecto/" + proyectoId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<AsignacionDTO>>() {})
                .block();
        
        if (raw != null) {
            for (AsignacionDTO asig : raw) {
                try {
                    EmpleadoDTO emp = obtenerEmpleadoPorId(asig.getEmpleadoId());
                    if (emp != null) {
                        asig.setNombreEmpleado(emp.getNombre());
                        asig.setCargoEmpleado(emp.getCargo());
                    }
                } catch (Exception e) {
                    asig.setNombreEmpleado("Empleado no encontrado");
                }
            }
        }
        return raw;
    }

    private void sincronizarHorasEmpleado(Long empleadoId) {
        try {
            List<AsignacionDTO> asignaciones = webClient.get()
                    .uri(urlProyectos + "/asignaciones/empleado/" + empleadoId)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<AsignacionDTO>>() {})
                    .block();
            
            int totalHoras = 0;
            if (asignaciones != null) {
                totalHoras = asignaciones.stream()
                        .mapToInt(AsignacionDTO::getHorasAsignadas)
                        .sum();
            }
            
            EmpleadoDTO emp = obtenerEmpleadoPorId(empleadoId);
            if (emp != null) {
                emp.setHorasAsignadas(totalHoras);
                webClient.put()
                        .uri(urlRecursos + "/" + empleadoId)
                        .bodyValue(emp)
                        .retrieve()
                        .bodyToMono(EmpleadoDTO.class)
                        .block();
            }
        } catch (Exception e) {
            System.err.println("Error al sincronizar horas de empleado " + empleadoId + ": " + e.getMessage());
        }
    }

    public AsignacionDTO crearAsignacion(AsignacionDTO asignacion) {
        AsignacionDTO creada = webClient.post()
                .uri(urlProyectos + "/asignaciones")
                .bodyValue(asignacion)
                .retrieve()
                .bodyToMono(AsignacionDTO.class)
                .block();
        
        if (creada != null) {
            sincronizarHorasEmpleado(creada.getEmpleadoId());
        }
        return creada;
    }

    public AsignacionDTO actualizarAsignacion(Long id, AsignacionDTO asignacion) {
        AsignacionDTO actualizada = webClient.put()
                .uri(urlProyectos + "/asignaciones/" + id)
                .bodyValue(asignacion)
                .retrieve()
                .bodyToMono(AsignacionDTO.class)
                .block();
        
        if (actualizada != null) {
            sincronizarHorasEmpleado(actualizada.getEmpleadoId());
        }
        return actualizada;
    }

    public void eliminarAsignacionPorProyectoYEmpleado(Long proyectoId, Long empleadoId) {
        webClient.delete()
                .uri(urlProyectos + "/asignaciones/proyecto/" + proyectoId + "/empleado/" + empleadoId)
                .retrieve()
                .toBodilessEntity()
                .block();
        
        sincronizarHorasEmpleado(empleadoId);
    }
}