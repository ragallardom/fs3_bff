package cl.innovatech.bffservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpleadoDTO {
    private Long id;
    private String nombre;
    private String cargo;
    private double capacity;
}