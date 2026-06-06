package cl.innovatech.bffservice.controller;

import cl.innovatech.bffservice.dto.ProyectoDTO;
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
public class ProyectoControllerTest {

    @Mock
    private BffService bffService;

    @InjectMocks
    private ProyectoController proyectoController;

    @Test
    void testObtenerTodos_DebeRetornarLista() {
        ProyectoDTO proj = ProyectoDTO.builder().id(1L).nombre("Proj").build();
        when(bffService.obtenerTodosProyectos()).thenReturn(Collections.singletonList(proj));

        ResponseEntity<List<ProyectoDTO>> response = proyectoController.obtenerTodos();
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testObtenerPorId_DebeRetornarProyecto() {
        ProyectoDTO proj = ProyectoDTO.builder().id(1L).nombre("Proj").build();
        when(bffService.obtenerProyectoPorId(1L)).thenReturn(proj);

        ResponseEntity<ProyectoDTO> response = proyectoController.obtenerPorId(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Proj", response.getBody().getNombre());
    }

    @Test
    void testCrear_DebeRetornar201() {
        ProyectoDTO proj = ProyectoDTO.builder().nombre("Proj").build();
        when(bffService.crearProyecto(any(ProyectoDTO.class))).thenReturn(proj);

        ResponseEntity<ProyectoDTO> response = proyectoController.crear(proj);
        assertNotNull(response);
        assertEquals(201, response.getStatusCode().value());
    }

    @Test
    void testActualizar_DebeRetornar200() {
        ProyectoDTO proj = ProyectoDTO.builder().id(1L).nombre("Updated").build();
        when(bffService.actualizarProyecto(eq(1L), any(ProyectoDTO.class))).thenReturn(proj);

        ResponseEntity<ProyectoDTO> response = proyectoController.actualizar(1L, proj);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void testEliminar_DebeRetornar204() {
        doNothing().when(bffService).eliminarProyecto(1L);

        ResponseEntity<Void> response = proyectoController.eliminar(1L);
        assertNotNull(response);
        assertEquals(204, response.getStatusCode().value());
    }
}
