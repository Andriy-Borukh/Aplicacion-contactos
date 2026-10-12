package andriy.com.aplicacion_contactos.dtos;

import java.util.List;

public record ImportacionContactosResponseDTO(
        int importados,
        int errores,
        List<String> detalles
) {
}