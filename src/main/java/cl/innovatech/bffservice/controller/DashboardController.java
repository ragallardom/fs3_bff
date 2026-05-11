package cl.innovatech.bffservice.controller;

import cl.innovatech.bffservice.dto.ProyectoResumenDTO;
import cl.innovatech.bffservice.service.BffService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final BffService bffService;

    public DashboardController(BffService bffService) {
        this.bffService = bffService;
    }

    @GetMapping("/resumen")
    public ResponseEntity<ProyectoResumenDTO> obtenerResumen(
            @RequestParam Long proyectoId,
            @RequestParam Long recursoId) {

        return ResponseEntity.ok(bffService.obtenerResumenCompleto(proyectoId, recursoId));
    }
}
