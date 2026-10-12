package andriy.com.aplicacion_contactos.controller;

import andriy.com.aplicacion_contactos.dtos.ContactoRegistroDTO;
import andriy.com.aplicacion_contactos.dtos.ImportacionContactosResponseDTO;
import andriy.com.aplicacion_contactos.entities.Usuario;
import andriy.com.aplicacion_contactos.service.ContactoService;
import andriy.com.aplicacion_contactos.service.UsuarioService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/contactos")
public class ContactoImportacionController {

    private final ContactoService contactoService;
    private final UsuarioService usuarioService;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    public ContactoImportacionController(
            ContactoService contactoService,
            UsuarioService usuarioService,
            ObjectMapper objectMapper,
            Validator validator
    ) {
        this.contactoService = contactoService;
        this.usuarioService = usuarioService;
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    @PostMapping(
            value = "/importar",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ImportacionContactosResponseDTO> importar(
            @RequestParam("archivo") MultipartFile archivo,
            Authentication authentication
    ) throws IOException {

        if (archivo.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new ImportacionContactosResponseDTO(
                            0, 1, List.of("El archivo está vacío")
                    )
            );
        }

        String nombreArchivo = archivo.getOriginalFilename();

        if (nombreArchivo == null) {
            return ResponseEntity.badRequest().body(
                    new ImportacionContactosResponseDTO(
                            0, 1, List.of("El archivo no tiene nombre")
                    )
            );
        }

        String nombre = nombreArchivo.toLowerCase();

        if (!nombre.endsWith(".csv") && !nombre.endsWith(".json")) {
            return ResponseEntity.badRequest().body(
                    new ImportacionContactosResponseDTO(
                            0, 1, List.of("Solo se admiten archivos CSV y JSON")
                    )
            );
        }

        List<ContactoRegistroDTO> contactos;

        try {
            if (nombre.endsWith(".json")) {
                contactos = leerJSON(archivo);
            } else {
                contactos = leerCSV(archivo);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new ImportacionContactosResponseDTO(
                            0, 1,
                            List.of("No se ha podido leer el archivo. Comprueba su formato.")
                    )
            );
        }

        Usuario usuario = usuarioService.obtenerPorEmail(
                authentication.getName()
        );

        int importados = 0;
        int errores = 0;
        List<String> detalles = new ArrayList<>();

        for (int i = 0; i < contactos.size(); i++) {
            ContactoRegistroDTO dto = contactos.get(i);
            int fila = i + 2;

            Set<ConstraintViolation<ContactoRegistroDTO>> violaciones =
                    validator.validate(dto);

            if (!violaciones.isEmpty()) {
                errores++;

                String mensajes = violaciones.stream()
                        .map(ConstraintViolation::getMessage)
                        .distinct()
                        .reduce((a, b) -> a + "; " + b)
                        .orElse("Datos no válidos");

                detalles.add("Registro " + fila + ": " + mensajes);
                continue;
            }

            try {
                contactoService.guardarContacto(dto, usuario);
                importados++;
            } catch (Exception e) {
                errores++;
                detalles.add(
                        "Registro " + fila
                                + ": no se ha podido guardar el contacto"
                );
            }
        }

        return ResponseEntity.ok(
                new ImportacionContactosResponseDTO(
                        importados, errores, detalles
                )
        );
    }

    private List<ContactoRegistroDTO> leerCSV(
            MultipartFile archivo
    ) throws IOException {

        List<ContactoRegistroDTO> contactos = new ArrayList<>();

        try (BufferedReader lector = new BufferedReader(
                new InputStreamReader(
                        archivo.getInputStream(),
                        StandardCharsets.UTF_8
                )
        )) {
            String cabecera = lector.readLine();

            if (cabecera == null) {
                throw new IOException("CSV vacío");
            }

            List<String> columnas = parsearLineaCSV(cabecera);

            List<String> esperadas = List.of(
                    "nombre", "apellidos", "telefono", "email",
                    "empresa", "cargo", "notas", "direccion",
                    "fechaCumpleanos", "favorito", "etiquetas"
            );

            if (!columnas.equals(esperadas)) {
                throw new IOException("Cabeceras CSV no válidas");
            }

            String linea;
            int numeroFila = 1;

            while ((linea = lector.readLine()) != null) {
                numeroFila++;

                if (linea.isBlank()) {
                    continue;
                }

                List<String> valores = parsearLineaCSV(linea);

                if (valores.size() != columnas.size()) {
                    throw new IOException(
                            "Número de columnas incorrecto en fila "
                                    + numeroFila
                    );
                }

                String fecha = valores.get(8).trim();
                LocalDate fechaCumpleanos = null;

                try {
                    if (!fecha.isEmpty()) {
                        fechaCumpleanos = LocalDate.parse(fecha);
                    }
                } catch (java.time.DateTimeException ignored) {

                }

                String valorFavorito = valores.get(9).trim();
                boolean favorito = valorFavorito.equalsIgnoreCase("true");

                Set<String> etiquetas = new HashSet<>();

                if (!valores.get(10).isBlank()) {
                    for (String etiqueta : valores.get(10).split(";")) {
                        if (!etiqueta.isBlank()) {
                            etiquetas.add(etiqueta.trim());
                        }
                    }
                }

                contactos.add(new ContactoRegistroDTO(
                        valores.get(0),
                        valores.get(1),
                        valores.get(2),
                        valores.get(3),
                        valores.get(4),
                        valores.get(5),
                        valores.get(6),
                        valores.get(7),
                        fechaCumpleanos,
                        favorito,
                        etiquetas
                ));
            }
        }

        return contactos;
    }

    private List<String> parsearLineaCSV(String linea)
            throws IOException {

        List<String> valores = new ArrayList<>();
        StringBuilder campo = new StringBuilder();
        boolean entreComillas = false;

        for (int i = 0; i < linea.length(); i++) {
            char c = linea.charAt(i);

            if (c == '"') {
                if (entreComillas
                        && i + 1 < linea.length()
                        && linea.charAt(i + 1) == '"') {
                    campo.append('"');
                    i++;
                } else {
                    entreComillas = !entreComillas;
                }
            } else if (c == ',' && !entreComillas) {
                valores.add(campo.toString());
                campo.setLength(0);
            } else {
                campo.append(c);
            }
        }

        if (entreComillas) {
            throw new IOException("Comillas CSV sin cerrar");
        }

        valores.add(campo.toString());
        return valores;
    }

    private List<ContactoRegistroDTO> leerJSON(
            MultipartFile archivo
    ) throws IOException {

        JsonNode raiz = objectMapper.readTree(archivo.getInputStream());

        if (raiz == null || !raiz.isArray()) {
            throw new IOException("El JSON debe contener una lista");
        }

        List<ContactoRegistroDTO> contactos = new ArrayList<>();

        for (JsonNode nodo : raiz) {
            contactos.add(new ContactoRegistroDTO(
                    texto(nodo, "nombre"),
                    texto(nodo, "apellidos"),
                    texto(nodo, "telefono"),
                    texto(nodo, "email"),
                    texto(nodo, "empresa"),
                    texto(nodo, "cargo"),
                    texto(nodo, "notas"),
                    texto(nodo, "direccion"),
                    fecha(nodo, "fechaCumpleanos"),
                    favorito(nodo),
                    etiquetas(nodo)
            ));
        }

        return contactos;
    }

    private String texto(JsonNode nodo, String campo) {
        JsonNode valor = nodo.path(campo);

        if (valor.isMissingNode() || valor.isNull()) {
            return null;
        }

        if (!valor.isValueNode()) {
            return null;
        }

        return valor.asText();
    }

    private LocalDate fecha(JsonNode nodo, String campo) {
        String valor = texto(nodo, campo);

        if (valor == null || valor.isBlank()) {
            return null;
        }

        try {
            return LocalDate.parse(valor.trim());
        } catch (java.time.DateTimeException e) {
            return null;
        }
    }

    private boolean favorito(JsonNode nodo) {
        String valor = texto(nodo, "favorito");

        if (valor == null) {
            return false;
        }

        if (valor.equalsIgnoreCase("true")) {
            return true;
        }

        return false;
    }

    private Set<String> etiquetas(JsonNode nodo) {
        JsonNode valores = nodo.path("etiquetas");
        Set<String> resultado = new HashSet<>();

        if (valores.isArray()) {
            for (JsonNode valor : valores) {
                if (valor.isTextual() && !valor.asText().isBlank()) {
                    resultado.add(valor.asText().trim());
                }
            }
        } else if (valores.isTextual()) {
            for (String etiqueta : valores.asText().split(";")) {
                if (!etiqueta.isBlank()) {
                    resultado.add(etiqueta.trim());
                }
            }
        }

        return resultado;
    }
}