package andriy.com.aplicacion_contactos.dtos;

import java.time.LocalDate;
import java.util.Set;

public record ContactoResponseDTO (

        Long id,

        String nombre,

        String apellidos,

        String telefono,

        String email,

        String empresa,

        String cargo,

        String notas,

        String direccion,

        LocalDate fechaCumpleanos,

        boolean favorito,

        Set<String> etiquetas

) {
}
