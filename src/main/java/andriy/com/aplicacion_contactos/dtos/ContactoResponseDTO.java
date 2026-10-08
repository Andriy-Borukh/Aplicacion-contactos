package andriy.com.aplicacion_contactos.dtos;

import andriy.com.aplicacion_contactos.entities.Usuario;

public record ContactoResponseDTO (
        Long id,
        String nombre,
        String apellidos,
        String telefono,
        String email
) {
}
