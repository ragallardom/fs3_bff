package cl.innovatech.bffservice.controller;

import cl.innovatech.bffservice.dto.ProyectoResumenDTO;
import cl.innovatech.bffservice.service.BffService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DashboardControllerTest {

    @Mock
    private BffService bffService;

    @InjectMocks
    private DashboardController dashboardController;

    @Test
    void testObtenerResumen_DebeRetornarResumen() {
        ProyectoResumenDTO dto = new ProyectoResumenDTO();
        dto.setNombreProyecto("Test Proj");
        when(bffService.obtenerResumenCompleto(1L, 2L)).thenReturn(dto);

        ResponseEntity<ProyectoResumenDTO> response = dashboardController.obtenerResumen(1L, 2L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Test Proj", response.getBody().getNombreProyecto());
    }
}
