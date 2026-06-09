package cl.innovatech.bffservice.controller;

import cl.innovatech.bffservice.dto.NotificacionDTO;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificacionControllerTest {

    @Mock
    private BffService bffService;

    @InjectMocks
    private NotificacionController notificacionController;

    @Test
    void testObtenerTodas_DebeRetornarLista() {
        NotificacionDTO notif = new NotificacionDTO();
        notif.setId(1L);
        notif.setMensaje("Test notification");
        when(bffService.obtenerNotificacionesCarga()).thenReturn(Collections.singletonList(notif));

        ResponseEntity<List<NotificacionDTO>> response = notificacionController.obtenerTodas();
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testMarcarComoLeida_DebeRetornarNotificacion() {
        NotificacionDTO notif = new NotificacionDTO();
        notif.setId(1L);
        notif.setMensaje("Test notification");
        when(bffService.marcarNotificacionComoLeida(1L)).thenReturn(notif);

        ResponseEntity<NotificacionDTO> response = notificacionController.marcarComoLeida(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
    }
}
