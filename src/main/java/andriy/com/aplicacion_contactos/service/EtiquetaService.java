package andriy.com.aplicacion_contactos.service;

import andriy.com.aplicacion_contactos.dtos.ContactoEtiquetaDTO;
import andriy.com.aplicacion_contactos.dtos.EtiquetaRegistroDTO;
import andriy.com.aplicacion_contactos.dtos.EtiquetaResponseDTO;
import andriy.com.aplicacion_contactos.entities.Etiqueta;
import andriy.com.aplicacion_contactos.entities.Usuario;
import andriy.com.aplicacion_contactos.exceptions.RecursoNoEncontrado;
import andriy.com.aplicacion_contactos.repositories.ContactoRepository;
import andriy.com.aplicacion_contactos.repositories.EtiquetaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EtiquetaService {

    private final EtiquetaRepository etiquetaRepository;
    private final UsuarioService usuarioService;
    private final ContactoRepository contactoRepository;

    public EtiquetaService(EtiquetaRepository etiquetaRepository,
                           UsuarioService usuarioService,
                           ContactoRepository contactoRepository) {
        this.etiquetaRepository = etiquetaRepository;
        this.usuarioService = usuarioService;
        this.contactoRepository = contactoRepository;
    }

    public EtiquetaResponseDTO registrarEtiqueta(
            EtiquetaRegistroDTO dto,
            String emailUsuario
    ) {
        Usuario usuario = usuarioService.obtenerPorEmail(emailUsuario);

        String nombre = dto.nombre().trim();

        if (etiquetaRepository.existsByUsuarioAndNombre(usuario, nombre)){
            throw new IllegalArgumentException("Ya hay una etiqueta con ese nombre");
        }

        Etiqueta etiqueta = new Etiqueta();

        etiqueta.setNombre(nombre);
        etiqueta.setUsuario(usuario);

        Etiqueta guardado = etiquetaRepository.save(etiqueta);

        return convertirDTO(guardado);

    }

    public List<EtiquetaResponseDTO> listar(String emailUsuario) {

        Usuario usuario = usuarioService.obtenerPorEmail(emailUsuario);

        return etiquetaRepository.findByUsuario(usuario)
                .stream()
                .map(this::convertirDTO) .toList();

    }

    @Transactional
    public void eliminar(Long id, String emailUsuario) {
        Usuario usuario = usuarioService.obtenerPorEmail(emailUsuario);

        Etiqueta etiqueta = etiquetaRepository.findById(id)
                .filter(e -> e.getUsuario().getId().equals(usuario.getId()))
                .orElseThrow(() ->
                        new RecursoNoEncontrado("No se ha encontrado la etiqueta")
                );

        contactoRepository.eliminarAsociacionesEtiqueta(etiqueta.getId());

        etiquetaRepository.delete(etiqueta);
    }

    private EtiquetaResponseDTO convertirDTO(Etiqueta etiqueta) {

        return new EtiquetaResponseDTO(etiqueta.getId(), etiqueta.getNombre());

    }

    public List<ContactoEtiquetaDTO> obtenerContactosAsociados(
            Long etiquetaId,
            String emailUsuario
    ) {
        Usuario usuario = usuarioService.obtenerPorEmail(emailUsuario);

        Etiqueta etiqueta = etiquetaRepository.findById(etiquetaId)
                .filter(e -> e.getUsuario().getId().equals(usuario.getId()))
                .orElseThrow(() ->
                        new RecursoNoEncontrado("No se ha encontrado la etiqueta")
                );

        return contactoRepository
                .findByUsuarioAndEtiquetas_Id(usuario, etiqueta.getId())
                .stream()
                .map(contacto -> new ContactoEtiquetaDTO(
                        contacto.getId(),
                        contacto.getNombre(),
                        contacto.getApellidos(),
                        contacto.getEmpresa()
                ))
                .toList();
    }

    public Set<Etiqueta> obtenerOCrearPorNombres(
            Set<String> nombres,
            Usuario usuario
    ) {
        if (nombres == null || nombres.isEmpty()) {
            return new java.util.HashSet<>();
        }

        return nombres.stream()
                .filter(java.util.Objects::nonNull)
                .map(String::trim)
                .filter(nombre -> !nombre.isEmpty())
                .distinct()
                .map(nombre -> etiquetaRepository
                        .findByUsuarioAndNombre(usuario, nombre)
                        .orElseGet(() -> {
                            Etiqueta etiqueta = new Etiqueta();
                            etiqueta.setNombre(nombre);
                            etiqueta.setUsuario(usuario);
                            return etiquetaRepository.save(etiqueta);
                        }))
                .collect(Collectors.toSet());
    }

}
