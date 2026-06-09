package cl.innovatech.bffservice.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificacionDTO {
    private Long id;
    private String titulo;
    private String mensaje;
    private String tipo;
    private LocalDateTime fecha;
    private Boolean leida;
}
