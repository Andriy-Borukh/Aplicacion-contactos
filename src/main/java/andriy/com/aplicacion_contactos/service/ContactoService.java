package andriy.com.aplicacion_contactos.service;

import andriy.com.aplicacion_contactos.dtos.ContactoRegistroDTO;
import andriy.com.aplicacion_contactos.dtos.ContactoResponseDTO;
import andriy.com.aplicacion_contactos.entities.Contacto;
import andriy.com.aplicacion_contactos.entities.Etiqueta;
import andriy.com.aplicacion_contactos.entities.Usuario;
import andriy.com.aplicacion_contactos.exceptions.RecursoNoEncontrado;
import andriy.com.aplicacion_contactos.repositories.ContactoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ContactoService {

    private final ContactoRepository contactoRepository;
    private final EtiquetaService etiquetaService;

    public ContactoService(ContactoRepository contactoRepository,
                           EtiquetaService etiquetaService) {
        this.contactoRepository = contactoRepository;
        this.etiquetaService = etiquetaService;
    }

    public List<ContactoResponseDTO> obtenerContactos(Usuario usuario) {

        return contactoRepository.findByUsuario(usuario)
                .stream()
                .map(contacto -> new ContactoResponseDTO(
                        contacto.getId(),
                        contacto.getNombre(),
                        contacto.getApellidos(),
                        contacto.getTelefono(),
                        contacto.getEmail(),
                        contacto.getEmpresa(),
                        contacto.getCargo(),
                        contacto.getNotas(),
                        contacto.getDireccion(),
                        contacto.getFechaCumple(),
                        contacto.getFavorito(),
                        contacto.getEtiquetas()
                                .stream()
                                .map(Etiqueta::getNombre)
                                .collect(Collectors.toSet())
                ))
                .toList();
    }

    public ContactoResponseDTO obtenerContacto(Long id, Usuario usuario) {

        Contacto contacto = contactoRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new RecursoNoEncontrado("No se ha encontrado el contacto"));

        return new ContactoResponseDTO(
                contacto.getId(),
                contacto.getNombre(),
                contacto.getApellidos(),
                contacto.getTelefono(),
                contacto.getEmail(),
                contacto.getEmpresa(),
                contacto.getCargo(),
                contacto.getNotas(),
                contacto.getDireccion(),
                contacto.getFechaCumple(),
                contacto.getFavorito(),
                contacto.getEtiquetas()
                        .stream()
                        .map(Etiqueta::getNombre)
                        .collect(Collectors.toSet())
        );
    }

    public ContactoResponseDTO guardarContacto(
            ContactoRegistroDTO dto,
            Usuario usuario
    ) {
        Contacto contacto = new Contacto();

        contacto.setNombre(dto.nombre());
        contacto.setApellidos(dto.apellidos());
        contacto.setTelefono(dto.telefono());
        contacto.setEmail(dto.email());
        contacto.setEmpresa(dto.empresa());
        contacto.setCargo(dto.cargo());
        contacto.setNotas(dto.notas());
        contacto.setDireccion(dto.direccion());
        contacto.setFechaCumple(dto.fechaCumpleanos());
        contacto.setFavorito(dto.favorito());

        Set<Etiqueta> etiquetas = etiquetaService.obtenerPorNombres(
                dto.etiquetas(),
                usuario
        );

        contacto.setEtiquetas(etiquetas);
        contacto.setUsuario(usuario);

        Contacto contactoGuardado = contactoRepository.save(contacto);

        return new ContactoResponseDTO(
                contactoGuardado.getId(),
                contactoGuardado.getNombre(),
                contactoGuardado.getApellidos(),
                contactoGuardado.getTelefono(),
                contactoGuardado.getEmail(),
                contactoGuardado.getEmpresa(),
                contactoGuardado.getCargo(),
                contactoGuardado.getNotas(),
                contactoGuardado.getDireccion(),
                contactoGuardado.getFechaCumple(),
                contactoGuardado.getFavorito(),
                contactoGuardado.getEtiquetas()
                        .stream()
                        .map(Etiqueta::getNombre)
                        .collect(Collectors.toSet())
        );
    }

    public void eliminarContacto(Long id, Usuario usuario) {

        Contacto contacto = contactoRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new RecursoNoEncontrado("Contacto no encontrado"));

        contactoRepository.delete(contacto);
    }

    public void editarContacto(
            Long id,
            ContactoRegistroDTO dto,
            Usuario usuario
    ) {
        Contacto contacto = contactoRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() ->
                        new RecursoNoEncontrado("Contacto no encontrado")
                );

        contacto.setNombre(dto.nombre());
        contacto.setApellidos(dto.apellidos());
        contacto.setTelefono(dto.telefono());
        contacto.setEmail(dto.email());
        contacto.setEmpresa(dto.empresa());
        contacto.setCargo(dto.cargo());
        contacto.setNotas(dto.notas());
        contacto.setDireccion(dto.direccion());
        contacto.setFechaCumple(dto.fechaCumpleanos());
        contacto.setFavorito(dto.favorito());
        Set<Etiqueta> etiquetas = etiquetaService.obtenerPorNombres(
                dto.etiquetas(),
                usuario
        );

        contacto.setEtiquetas(etiquetas);

        contactoRepository.save(contacto);
    }

}
