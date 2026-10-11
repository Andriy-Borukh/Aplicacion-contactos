package andriy.com.aplicacion_contactos.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EtiquetaRegistroDTO(
        @NotNull(message = "El nombre de la etiqueta es obligatorio")
        @Size(max = 50, message = "La etiqueta no puede superar los 50 caracteres")
        String nombre
) {
}
