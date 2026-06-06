package cl.innovatech.bffservice.controller;

import cl.innovatech.bffservice.dto.AsignacionDTO;
import cl.innovatech.bffservice.service.BffService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asignaciones")
@CrossOrigin(origins = "http://localhost:3000")
public class AsignacionController {

    private final BffService bffService;

    public AsignacionController(BffService bffService) {
        this.bffService = bffService;
    }

    @GetMapping("/proyecto/{proyectoId}")
    public ResponseEntity<List<AsignacionDTO>> obtenerPorProyecto(@PathVariable Long proyectoId) {
        return ResponseEntity.ok(bffService.obtenerAsignacionesPorProyecto(proyectoId));
    }

    @PostMapping
    public ResponseEntity<AsignacionDTO> crear(@RequestBody AsignacionDTO asignacion) {
        return ResponseEntity.status(201).body(bffService.crearAsignacion(asignacion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AsignacionDTO> actualizar(@PathVariable Long id, @RequestBody AsignacionDTO asignacion) {
        return ResponseEntity.ok(bffService.actualizarAsignacion(id, asignacion));
    }

    @DeleteMapping("/proyecto/{proyectoId}/empleado/{empleadoId}")
    public ResponseEntity<Void> eliminarPorProyectoYEmpleado(@PathVariable Long proyectoId, @PathVariable Long empleadoId) {
        bffService.eliminarAsignacionPorProyectoYEmpleado(proyectoId, empleadoId);
        return ResponseEntity.noContent().build();
    }
}
