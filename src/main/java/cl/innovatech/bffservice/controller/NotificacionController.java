package cl.innovatech.bffservice.controller;

import cl.innovatech.bffservice.dto.NotificacionDTO;
import cl.innovatech.bffservice.service.BffService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
@CrossOrigin(origins = "http://localhost:3000")
public class NotificacionController {

    private final BffService bffService;

    public NotificacionController(BffService bffService) {
        this.bffService = bffService;
    }

    @GetMapping
    public ResponseEntity<List<NotificacionDTO>> obtenerTodas() {
        return ResponseEntity.ok(bffService.obtenerNotificacionesCarga());
    }

    @PutMapping("/{id}/leer")
    public ResponseEntity<NotificacionDTO> marcarComoLeida(@PathVariable Long id) {
        return ResponseEntity.ok(bffService.marcarNotificacionComoLeida(id));
    }
}
