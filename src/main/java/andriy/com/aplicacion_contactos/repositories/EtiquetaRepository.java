package andriy.com.aplicacion_contactos.repositories;

import andriy.com.aplicacion_contactos.entities.Etiqueta;
import andriy.com.aplicacion_contactos.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EtiquetaRepository extends JpaRepository<Etiqueta, Long> {

    Optional<Etiqueta> findByNombre(String nombre);

    List<Etiqueta> findByUsuario(Usuario usuario);

    Optional<Etiqueta> findByUsuarioAndNombre(Usuario usuario, String nombre);

    boolean existsByNombre(String nombre);

    boolean existsByUsuarioAndNombre(Usuario usuario, String nombre);

}
