package cl.innovatech.bffservice.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardSummaryDTO {
    private List<EmpleadoDTO> empleadosDisponibles;
    private double capacidadGlobalPromedio;

    private List<ProyectoDTO> proyectosCriticos;

    private String mensajeEstado;
}