package cl.innovatech.bffservice.controller;

import cl.innovatech.bffservice.dto.EmpleadoDTO;
import cl.innovatech.bffservice.service.BffService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
@CrossOrigin(origins = "http://localhost:3000")
public class EmpleadoController {

    private final BffService bffService;

    public EmpleadoController(BffService bffService) {
        this.bffService = bffService;
    }

    @GetMapping
    public ResponseEntity<List<EmpleadoDTO>> obtenerTodos() {
        return ResponseEntity.ok(bffService.obtenerTodosEmpleados());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(bffService.obtenerEmpleadoPorId(id));
    }

    @PostMapping
    public ResponseEntity<EmpleadoDTO> crear(@RequestBody EmpleadoDTO empleado) {
        return ResponseEntity.status(201).body(bffService.crearEmpleado(empleado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpleadoDTO> actualizar(@PathVariable Long id, @RequestBody EmpleadoDTO empleado) {
        return ResponseEntity.ok(bffService.actualizarEmpleado(id, empleado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        bffService.eliminarEmpleado(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/capacity")
    public ResponseEntity<Double> obtenerCapacidad(@PathVariable Long id) {
        return ResponseEntity.ok(bffService.obtenerCapacidadEmpleado(id));
    }
}
