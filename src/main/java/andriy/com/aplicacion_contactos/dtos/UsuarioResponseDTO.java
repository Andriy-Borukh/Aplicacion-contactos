package andriy.com.aplicacion_contactos.dtos;

import java.time.LocalDate;

public record UsuarioResponseDTO (
        Long id,
        String nombre,
        String email,
        LocalDate fechaRegistro
) {
}