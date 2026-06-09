package cl.innovatech.bffservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsignacionDTO {
    private Long id;
    private Long proyectoId;
    private Long empleadoId;
    private Integer horasAsignadas;
    
    // Enriched fields for the frontend
    private String nombreEmpleado;
    private String cargoEmpleado;
    private String nombreProyecto;
}
