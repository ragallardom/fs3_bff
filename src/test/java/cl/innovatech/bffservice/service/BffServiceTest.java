package cl.innovatech.bffservice.service;

import cl.innovatech.bffservice.dto.AsignacionDTO;
import cl.innovatech.bffservice.dto.EmpleadoDTO;
import cl.innovatech.bffservice.dto.ProyectoDTO;
import cl.innovatech.bffservice.dto.ProyectoResumenDTO;
import cl.innovatech.bffservice.dto.RecursoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BffServiceTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private BffService bffService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(bffService, "urlRecursos", "http://localhost:8081/api/rrhh/empleados");
        ReflectionTestUtils.setField(bffService, "urlProyectos", "http://localhost:8082/api/proyectos");
        ReflectionTestUtils.setField(bffService, "urlNotifications", "http://localhost:8083/api/notificaciones");
    }

    @Test
    void testObtenerResumenCompleto() {
        ProyectoDTO proj = ProyectoDTO.builder().nombre("Proj").estado("ACTIVO").totalHoras(100).build();
        RecursoDTO rec = RecursoDTO.builder().nombre("Rec").cargo("Dev").build();
        Double cap = 40.0;

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("http://localhost:8082/api/proyectos/1/detalle")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ProyectoDTO.class)).thenReturn(Mono.just(proj));

        when(requestHeadersUriSpec.uri("http://localhost:8081/api/rrhh/empleados/2")).thenReturn(requestHeadersSpec);
        when(responseSpec.bodyToMono(RecursoDTO.class)).thenReturn(Mono.just(rec));

        when(requestHeadersUriSpec.uri("http://localhost:8081/api/rrhh/empleados/2/capacity")).thenReturn(requestHeadersSpec);
        when(responseSpec.bodyToMono(Double.class)).thenReturn(Mono.just(cap));

        ProyectoResumenDTO res = bffService.obtenerResumenCompleto(1L, 2L);

        assertNotNull(res);
        assertEquals("Proj", res.getNombreProyecto());
        assertEquals("Rec", res.getNombreResponsable());
        assertEquals(40.0, res.getCapacidadResponsable());
    }

    @Test
    void testObtenerTodosEmpleados_DebeRetornarListaConCapacidad() {
        EmpleadoDTO emp = EmpleadoDTO.builder()
                .id(1L)
                .nombre("Empleado Test")
                .horasAsignadas(10)
                .build();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("http://localhost:8081/api/rrhh/empleados")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class))).thenReturn(Mono.just(Collections.singletonList(emp)));

        when(requestHeadersUriSpec.uri("http://localhost:8081/api/rrhh/empleados/1/capacity")).thenReturn(requestHeadersSpec);
        when(responseSpec.bodyToMono(Double.class)).thenReturn(Mono.just(35.0));

        List<EmpleadoDTO> result = bffService.obtenerTodosEmpleados();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(35.0, result.get(0).getCapacity());
    }

    @Test
    void testCrearEmpleado() {
        EmpleadoDTO emp = EmpleadoDTO.builder().nombre("Emp").build();

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("http://localhost:8081/api/rrhh/empleados")).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(EmpleadoDTO.class)).thenReturn(Mono.just(emp));

        EmpleadoDTO res = bffService.crearEmpleado(emp);
        assertNotNull(res);
        assertEquals("Emp", res.getNombre());
    }

    @Test
    void testActualizarEmpleado() {
        EmpleadoDTO emp = EmpleadoDTO.builder().nombre("Emp").build();

        when(webClient.put()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("http://localhost:8081/api/rrhh/empleados/1")).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(EmpleadoDTO.class)).thenReturn(Mono.just(emp));

        EmpleadoDTO res = bffService.actualizarEmpleado(1L, emp);
        assertNotNull(res);
    }

    @Test
    void testEliminarEmpleado() {
        when(webClient.delete()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("http://localhost:8081/api/rrhh/empleados/1")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toBodilessEntity()).thenReturn(Mono.empty());

        bffService.eliminarEmpleado(1L);
        verify(webClient, times(1)).delete();
    }

    @Test
    void testObtenerProyectoPorId() {
        ProyectoDTO proj = ProyectoDTO.builder().nombre("Proj").build();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("http://localhost:8082/api/proyectos/1/detalle")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ProyectoDTO.class)).thenReturn(Mono.just(proj));

        ProyectoDTO res = bffService.obtenerProyectoPorId(1L);
        assertNotNull(res);
        assertEquals("Proj", res.getNombre());
    }

    @Test
    void testCrearProyecto() {
        ProyectoDTO proj = ProyectoDTO.builder().nombre("Proj").build();

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("http://localhost:8082/api/proyectos")).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ProyectoDTO.class)).thenReturn(Mono.just(proj));

        ProyectoDTO res = bffService.crearProyecto(proj);
        assertNotNull(res);
    }

    @Test
    void testActualizarProyecto() {
        ProyectoDTO proj = ProyectoDTO.builder().nombre("Proj").build();

        when(webClient.put()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("http://localhost:8082/api/proyectos/1")).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ProyectoDTO.class)).thenReturn(Mono.just(proj));

        ProyectoDTO res = bffService.actualizarProyecto(1L, proj);
        assertNotNull(res);
    }

    @Test
    void testEliminarProyecto() {
        when(webClient.delete()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("http://localhost:8082/api/proyectos/1")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toBodilessEntity()).thenReturn(Mono.empty());

        bffService.eliminarProyecto(1L);
        verify(webClient, times(1)).delete();
    }

    @Test
    void testObtenerAsignacionesPorProyecto() {
        AsignacionDTO asig = AsignacionDTO.builder().id(1L).empleadoId(2L).build();
        EmpleadoDTO emp = EmpleadoDTO.builder().nombre("Emp").cargo("Dev").build();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("http://localhost:8082/api/proyectos/asignaciones/proyecto/1")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class))).thenReturn(Mono.just(Collections.singletonList(asig)));

        when(requestHeadersUriSpec.uri("http://localhost:8081/api/rrhh/empleados/2")).thenReturn(requestHeadersSpec);
        when(responseSpec.bodyToMono(EmpleadoDTO.class)).thenReturn(Mono.just(emp));

        List<AsignacionDTO> res = bffService.obtenerAsignacionesPorProyecto(1L);
        assertNotNull(res);
        assertEquals(1, res.size());
        assertEquals("Emp", res.get(0).getNombreEmpleado());
    }

    @Test
    void testCrearAsignacion_DebeOrquestarYSincronizar() {
        AsignacionDTO asig = AsignacionDTO.builder()
                .proyectoId(1L)
                .empleadoId(1L)
                .horasAsignadas(15)
                .build();

        EmpleadoDTO emp = EmpleadoDTO.builder()
                .id(1L)
                .nombre("Empleado Test")
                .horasAsignadas(10)
                .build();

        // 1. Mock POST creation
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("http://localhost:8082/api/proyectos/asignaciones")).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(AsignacionDTO.class)).thenReturn(Mono.just(asig));

        // 2. Mock GET assignments list inside sincronizarHorasEmpleado
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("http://localhost:8082/api/proyectos/asignaciones/empleado/1")).thenReturn(requestHeadersSpec);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class))).thenReturn(Mono.just(Collections.singletonList(asig)));

        // 3. Mock GET employee by id
        when(requestHeadersUriSpec.uri("http://localhost:8081/api/rrhh/empleados/1")).thenReturn(requestHeadersSpec);
        when(responseSpec.bodyToMono(EmpleadoDTO.class)).thenReturn(Mono.just(emp));

        // 4. Mock PUT update employee
        when(webClient.put()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("http://localhost:8081/api/rrhh/empleados/1")).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(responseSpec.bodyToMono(EmpleadoDTO.class)).thenReturn(Mono.just(emp));

        AsignacionDTO result = bffService.crearAsignacion(asig);

        assertNotNull(result);
        assertEquals(15, result.getHorasAsignadas());
        verify(webClient, times(1)).post();
        verify(webClient, times(1)).put();
    }
}
