package cl.innovatech.bffservice.dto;

import lombok.Data;

@Data
public class ProyectoDTO {
    private Long id;
    private String nombre;
    private String estado;
    private Integer totalHoras;
}
