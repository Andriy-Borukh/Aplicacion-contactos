package andriy.com.aplicacion_contactos.service;

import andriy.com.aplicacion_contactos.dtos.ContactoRegistroDTO;
import andriy.com.aplicacion_contactos.dtos.ContactoResponseDTO;
import andriy.com.aplicacion_contactos.entities.Contacto;
import andriy.com.aplicacion_contactos.entities.Usuario;
import andriy.com.aplicacion_contactos.repositories.ContactoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactoService {

    private final ContactoRepository contactoRepository;

    public ContactoService(ContactoRepository contactoRepository) {
        this.contactoRepository = contactoRepository;
    }

    public List<ContactoResponseDTO> obtenerContactos(Usuario usuario) {
        return contactoRepository.findByUsuario(usuario)
                .stream()
                .map(contacto -> new ContactoResponseDTO(
                        contacto.getId(),
                        contacto.getNombre(),
                        contacto.getApellidos(),
                        contacto.getTelefono(),
                        contacto.getEmail()
                ))
                .toList();
    }

    public ContactoResponseDTO obtenerContacto(Long id, Usuario usuario) {

        Contacto contacto = contactoRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new IllegalArgumentException("No se ha encontrado el contacto"));

        return new ContactoResponseDTO(
                contacto.getId(),
                contacto.getNombre(),
                contacto.getApellidos(),
                contacto.getTelefono(),
                contacto.getEmail()
        );
    }

    public void guardarContacto(ContactoRegistroDTO dto, Usuario usuario) {

        Contacto contacto = new Contacto();

        contacto.setNombre(dto.nombre());
        contacto.setApellidos(dto.apellidos());
        contacto.setTelefono(dto.telefono());
        contacto.setEmail(dto.email());

        contacto.setUsuario(usuario);

        contactoRepository.save(contacto);
    }

    public void eliminarContacto(Long id, Usuario usuario) {

        Contacto contacto = contactoRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new IllegalArgumentException("Contacto no encontrado"));

        contactoRepository.delete(contacto);
    }

    public void editarContacto(
            Long id,
            ContactoRegistroDTO dto,
            Usuario usuario
    ) {
        Contacto contacto = contactoRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() ->
                        new IllegalArgumentException("Contacto no encontrado")
                );

        contacto.setNombre(dto.nombre());
        contacto.setApellidos(dto.apellidos());
        contacto.setTelefono(dto.telefono());
        contacto.setEmail(dto.email());

        contactoRepository.save(contacto);
    }

}
