package andriy.com.aplicacion_contactos.controller;

import andriy.com.aplicacion_contactos.dtos.UsuarioRegistroDTO;
import andriy.com.aplicacion_contactos.dtos.UsuarioResponseDTO;
import andriy.com.aplicacion_contactos.entities.Usuario;
import andriy.com.aplicacion_contactos.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioControllerRest {

    private final UsuarioService usuarioService;

    public UsuarioControllerRest(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> obtenerUsuario(Authentication authentication) {

        Usuario usuario = usuarioService.obtenerPorEmail(authentication.getName());

        UsuarioResponseDTO usuariodto = new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getFechaRegistro()
        );

        return ResponseEntity.ok(usuariodto);

    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> registrarUsuario(
            @Valid @RequestBody UsuarioRegistroDTO dto
    ) {

        usuarioService.registrarUsuario(dto);

        Usuario usuario = usuarioService.obtenerPorEmail(dto.email());

        UsuarioResponseDTO usuarioDTO = new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getFechaRegistro() );

        return ResponseEntity .status(HttpStatus.CREATED) .body(usuarioDTO);

    }

}
