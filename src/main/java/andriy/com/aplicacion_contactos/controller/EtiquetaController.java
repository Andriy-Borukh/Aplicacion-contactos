package andriy.com.aplicacion_contactos.controller;

import andriy.com.aplicacion_contactos.dtos.ContactoEtiquetaDTO;
import andriy.com.aplicacion_contactos.dtos.EtiquetaRegistroDTO;
import andriy.com.aplicacion_contactos.dtos.EtiquetaResponseDTO;
import andriy.com.aplicacion_contactos.service.EtiquetaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/etiquetas")
public class EtiquetaController {

    private final EtiquetaService etiquetaService;

    public EtiquetaController(EtiquetaService etiquetaService) {
        this.etiquetaService = etiquetaService;
    }

    @GetMapping
    public ResponseEntity<List<EtiquetaResponseDTO>> listar(
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                etiquetaService.listar(authentication.getName())
        );
    }

    @GetMapping("/{id}/contactos")
    public ResponseEntity<List<ContactoEtiquetaDTO>> listarContactosAsociados(
            @PathVariable Long id,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                etiquetaService.obtenerContactosAsociados(
                        id,
                        authentication.getName()
                )
        );
    }

    @PostMapping
    public ResponseEntity<EtiquetaResponseDTO> registrar(
            @Valid @RequestBody EtiquetaRegistroDTO dto,
            Authentication authentication
    ) {
        EtiquetaResponseDTO etiqueta = etiquetaService.registrarEtiqueta(
                dto,
                authentication.getName()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(etiqueta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id,
            Authentication authentication
    ) {
        etiquetaService.eliminar(id, authentication.getName());

        return ResponseEntity.noContent().build();
    }
}