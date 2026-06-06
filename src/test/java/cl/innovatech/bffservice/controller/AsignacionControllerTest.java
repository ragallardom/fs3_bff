package cl.innovatech.bffservice.controller;

import cl.innovatech.bffservice.dto.AsignacionDTO;
import cl.innovatech.bffservice.service.BffService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AsignacionControllerTest {

    @Mock
    private BffService bffService;

    @InjectMocks
    private AsignacionController asignacionController;

    @Test
    void testObtenerPorProyecto_DebeRetornarLista() {
        AsignacionDTO asig = AsignacionDTO.builder()
                .id(1L)
                .proyectoId(10L)
                .empleadoId(20L)
                .horasAsignadas(15)
                .build();

        when(bffService.obtenerAsignacionesPorProyecto(10L)).thenReturn(Collections.singletonList(asig));

        ResponseEntity<List<AsignacionDTO>> response = asignacionController.obtenerPorProyecto(10L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
        assertEquals(15, response.getBody().get(0).getHorasAsignadas());
        verify(bffService, times(1)).obtenerAsignacionesPorProyecto(10L);
    }

    @Test
    void testCrear_DebeRetornar201() {
        AsignacionDTO asig = AsignacionDTO.builder()
                .proyectoId(10L)
                .empleadoId(20L)
                .horasAsignadas(15)
                .build();

        when(bffService.crearAsignacion(any(AsignacionDTO.class))).thenReturn(asig);

        ResponseEntity<AsignacionDTO> response = asignacionController.crear(asig);

        assertNotNull(response);
        assertEquals(201, response.getStatusCode().value());
        assertEquals(15, response.getBody().getHorasAsignadas());
        verify(bffService, times(1)).crearAsignacion(any(AsignacionDTO.class));
    }

    @Test
    void testActualizar_DebeRetornar200() {
        AsignacionDTO asig = AsignacionDTO.builder()
                .id(1L)
                .proyectoId(10L)
                .empleadoId(20L)
                .horasAsignadas(20)
                .build();

        when(bffService.actualizarAsignacion(eq(1L), any(AsignacionDTO.class))).thenReturn(asig);

        ResponseEntity<AsignacionDTO> response = asignacionController.actualizar(1L, asig);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(20, response.getBody().getHorasAsignadas());
        verify(bffService, times(1)).actualizarAsignacion(eq(1L), any(AsignacionDTO.class));
    }

    @Test
    void testEliminar_DebeRetornar204() {
        doNothing().when(bffService).eliminarAsignacionPorProyectoYEmpleado(10L, 20L);

        ResponseEntity<Void> response = asignacionController.eliminarPorProyectoYEmpleado(10L, 20L);

        assertNotNull(response);
        assertEquals(204, response.getStatusCode().value());
        verify(bffService, times(1)).eliminarAsignacionPorProyectoYEmpleado(10L, 20L);
    }
}
