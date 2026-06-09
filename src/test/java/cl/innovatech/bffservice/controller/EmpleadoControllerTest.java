package cl.innovatech.bffservice.controller;

import cl.innovatech.bffservice.dto.EmpleadoDTO;
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
public class EmpleadoControllerTest {

    @Mock
    private BffService bffService;

    @InjectMocks
    private EmpleadoController empleadoController;

    @Test
    void testObtenerTodos_DebeRetornarLista() {
        EmpleadoDTO emp = EmpleadoDTO.builder().id(1L).nombre("Test").build();
        when(bffService.obtenerTodosEmpleados()).thenReturn(Collections.singletonList(emp));

        ResponseEntity<List<EmpleadoDTO>> response = empleadoController.obtenerTodos();
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testObtenerPorId_DebeRetornarEmpleado() {
        EmpleadoDTO emp = EmpleadoDTO.builder().id(1L).nombre("Test").build();
        when(bffService.obtenerEmpleadoPorId(1L)).thenReturn(emp);

        ResponseEntity<EmpleadoDTO> response = empleadoController.obtenerPorId(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Test", response.getBody().getNombre());
    }

    @Test
    void testCrear_DebeRetornar201() {
        EmpleadoDTO emp = EmpleadoDTO.builder().nombre("Test").build();
        when(bffService.crearEmpleado(any(EmpleadoDTO.class))).thenReturn(emp);

        ResponseEntity<EmpleadoDTO> response = empleadoController.crear(emp);
        assertNotNull(response);
        assertEquals(201, response.getStatusCode().value());
    }

    @Test
    void testActualizar_DebeRetornar200() {
        EmpleadoDTO emp = EmpleadoDTO.builder().id(1L).nombre("Updated").build();
        when(bffService.actualizarEmpleado(eq(1L), any(EmpleadoDTO.class))).thenReturn(emp);

        ResponseEntity<EmpleadoDTO> response = empleadoController.actualizar(1L, emp);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void testEliminar_DebeRetornar204() {
        doNothing().when(bffService).eliminarEmpleado(1L);

        ResponseEntity<Void> response = empleadoController.eliminar(1L);
        assertNotNull(response);
        assertEquals(204, response.getStatusCode().value());
    }

    @Test
    void testObtenerCapacidad_DebeRetornarCapacidad() {
        when(bffService.obtenerCapacidadEmpleado(1L)).thenReturn(40.0);

        ResponseEntity<Double> response = empleadoController.obtenerCapacidad(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(40.0, response.getBody());
    }
}
