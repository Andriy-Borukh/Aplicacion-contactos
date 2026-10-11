package andriy.com.aplicacion_contactos.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public record ContactoRegistroDTO(
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @NotBlank(message = "El apellido es obligatorio")
        String apellidos,

        @NotBlank(message = "El telefono es obligatorio")
        @Pattern(
                regexp = "^\\+[1-9]\\d{1,14}$",
                message = "El telefono no es valido"
        )
        String telefono,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no es valido")
        String email,

        String empresa,

        String cargo,

        String notas,

        String direccion,

        LocalDate fechaCumpleanos,

        boolean favorito,

        Set<String> etiquetas
) {

        public ContactoRegistroDTO {
                if (etiquetas == null) {
                        etiquetas = new HashSet<>();
                }
        }

}
