package cl.innovatech.bffservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProyectoDTO {
    private Long id;
    private String nombre;
    private String estado;
    private double progreso;
}