package cl.innovatech.bffservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProyectoResumenDTO {
    private String nombreProyecto;
    private String estadoProyecto;
    private Integer totalHoras;
    private String nombreResponsable;
    private String cargoResponsable;
    private Double capacidadResponsable;
}
