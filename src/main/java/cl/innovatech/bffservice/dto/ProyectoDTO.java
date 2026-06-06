package cl.innovatech.bffservice.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ProyectoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private LocalDate fechaInicio;
    private String estado;
    private Integer totalHoras;
}
