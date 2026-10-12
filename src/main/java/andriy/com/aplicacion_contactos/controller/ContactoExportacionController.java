package andriy.com.aplicacion_contactos.controller;

import andriy.com.aplicacion_contactos.dtos.ContactoResponseDTO;
import andriy.com.aplicacion_contactos.entities.Usuario;
import andriy.com.aplicacion_contactos.service.ContactoService;
import andriy.com.aplicacion_contactos.service.UsuarioService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/contactos/exportar")
public class ContactoExportacionController {

    private final ContactoService contactoService;
    private final UsuarioService usuarioService;
    private final ObjectMapper objectMapper;

    public ContactoExportacionController(
            ContactoService contactoService,
            UsuarioService usuarioService,
            ObjectMapper objectMapper
    ) {
        this.contactoService = contactoService;
        this.usuarioService = usuarioService;
        this.objectMapper = objectMapper;
    }

    @GetMapping(
            value = "/json",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<byte[]> exportarJSON(
            Authentication authentication
    ) throws Exception {

        Usuario usuario = usuarioService.obtenerPorEmail(
                authentication.getName()
        );

        List<ContactoResponseDTO> contactos =
                contactoService.obtenerContactos(usuario);

        byte[] archivo = objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsBytes(contactos);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"contactos.json\""
                )
                .contentType(MediaType.APPLICATION_JSON)
                .body(archivo);
    }

    @GetMapping(
            value = "/csv",
            produces = "text/csv"
    )
    public ResponseEntity<byte[]> exportarCSV(
            Authentication authentication
    ) {

        Usuario usuario = usuarioService.obtenerPorEmail(
                authentication.getName()
        );

        List<ContactoResponseDTO> contactos =
                contactoService.obtenerContactos(usuario);

        StringBuilder csv = new StringBuilder();

        csv.append(
                "nombre,apellidos,telefono,email,empresa,cargo,"
                        + "notas,direccion,fechaCumpleanos,favorito,etiquetas\r\n"
        );

        for (ContactoResponseDTO contacto : contactos) {
            csv.append(campoCSV(contacto.nombre())).append(',')
                    .append(campoCSV(contacto.apellidos())).append(',')
                    .append(campoCSV(contacto.telefono())).append(',')
                    .append(campoCSV(contacto.email())).append(',')
                    .append(campoCSV(contacto.empresa())).append(',')
                    .append(campoCSV(contacto.cargo())).append(',')
                    .append(campoCSV(contacto.notas())).append(',')
                    .append(campoCSV(contacto.direccion())).append(',')
                    .append(campoCSV(
                            contacto.fechaCumpleanos() == null
                                    ? ""
                                    : contacto.fechaCumpleanos().toString()
                    )).append(',')
                    .append(campoCSV(
                            Boolean.toString(contacto.favorito())
                    )).append(',')
                    .append(campoCSV(
                            contacto.etiquetas() == null
                                    ? ""
                                    : String.join("; ", contacto.etiquetas())
                    ))
                    .append("\r\n");
        }

        byte[] archivo = csv.toString()
                .getBytes(StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"contactos.csv\""
                )
                .contentType(new MediaType("text", "csv", StandardCharsets.UTF_8))
                .body(archivo);
    }

    private String campoCSV(String valor) {
        if (valor == null) {
            return "";
        }

        return "\"" + valor.replace("\"", "\"\"") + "\"";
    }
}