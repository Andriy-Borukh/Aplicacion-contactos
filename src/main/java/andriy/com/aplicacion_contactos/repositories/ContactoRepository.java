package andriy.com.aplicacion_contactos.repositories;

import andriy.com.aplicacion_contactos.entities.Contacto;
import andriy.com.aplicacion_contactos.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContactoRepository extends JpaRepository<Contacto, Long> {

    List<Contacto> findByUsuario(Usuario usuario);

    Optional<Contacto> findByIdAndUsuario(Long id, Usuario usuario);

}
