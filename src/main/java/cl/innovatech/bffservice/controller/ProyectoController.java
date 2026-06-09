package cl.innovatech.bffservice.controller;

import cl.innovatech.bffservice.dto.ProyectoDTO;
import cl.innovatech.bffservice.service.BffService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proyectos")
@CrossOrigin(origins = "http://localhost:3000")
public class ProyectoController {

    private final BffService bffService;

    public ProyectoController(BffService bffService) {
        this.bffService = bffService;
    }

    @GetMapping
    public ResponseEntity<List<ProyectoDTO>> obtenerTodos() {
        return ResponseEntity.ok(bffService.obtenerTodosProyectos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProyectoDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(bffService.obtenerProyectoPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProyectoDTO> crear(@RequestBody ProyectoDTO proyecto) {
        return ResponseEntity.status(201).body(bffService.crearProyecto(proyecto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProyectoDTO> actualizar(@PathVariable Long id, @RequestBody ProyectoDTO proyecto) {
        return ResponseEntity.ok(bffService.actualizarProyecto(id, proyecto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        bffService.eliminarProyecto(id);
        return ResponseEntity.noContent().build();
    }
}
