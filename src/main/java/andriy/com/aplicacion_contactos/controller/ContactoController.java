package andriy.com.aplicacion_contactos.controller;

import andriy.com.aplicacion_contactos.dtos.ContactoRegistroDTO;
import andriy.com.aplicacion_contactos.dtos.ContactoResponseDTO;
import andriy.com.aplicacion_contactos.entities.Usuario;
import andriy.com.aplicacion_contactos.service.ContactoService;
import andriy.com.aplicacion_contactos.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/contactos")
public class ContactoControllerREST {

    private final ContactoService contactoService;
    private final UsuarioService usuarioService;

    public ContactoControllerREST(
            ContactoService contactoService,
            UsuarioService usuarioService
    ) {
        this.contactoService = contactoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<ContactoResponseDTO>> obtenerContactos(
            Authentication authentication
    ) {

        Usuario usuario = usuarioService.obtenerPorEmail(authentication.getName());

        return ResponseEntity.ok(contactoService.obtenerContactos(usuario));

    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactoResponseDTO> obtenerContacto(
            @PathVariable Long id,
            Authentication authentication
    ) {

        Usuario usuario = usuarioService.obtenerPorEmail(authentication.getName());

        ContactoResponseDTO contacto = contactoService.obtenerContacto(id, usuario);

        return ResponseEntity.ok(contacto);
    }


    @PostMapping
    public ResponseEntity<ContactoResponseDTO> guardarContacto(
            @Valid @RequestBody ContactoRegistroDTO dto,
            Authentication authentication
    ) {
        Usuario usuario = usuarioService.obtenerPorEmail(
                authentication.getName()
        );

        ContactoResponseDTO contactoCreado =
                contactoService.guardarContacto(dto, usuario);

        URI ubicacion = URI.create(
                "/api/contactos/" + contactoCreado.id()
        );

        return ResponseEntity
                .created(ubicacion)
                .body(contactoCreado);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ContactoResponseDTO> editarContacto(
            @PathVariable Long id,
            @Valid @RequestBody ContactoRegistroDTO dto,
            Authentication authentication
    ) {
        Usuario usuario = usuarioService.obtenerPorEmail(
                authentication.getName()
        );

        contactoService.editarContacto(id, dto, usuario);

        ContactoResponseDTO contactoActualizado =
                contactoService.obtenerContacto(id, usuario);

        return ResponseEntity.ok(contactoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarContacto(
            @PathVariable Long id,
            Authentication authentication
    ) {
        Usuario usuario = usuarioService.obtenerPorEmail(
                authentication.getName()
        );

        contactoService.eliminarContacto(id, usuario);

        return ResponseEntity.noContent().build();
    }

}
