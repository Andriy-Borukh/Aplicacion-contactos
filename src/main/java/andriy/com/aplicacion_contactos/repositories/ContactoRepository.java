package andriy.com.aplicacion_contactos.repositories;

import andriy.com.aplicacion_contactos.entities.Contacto;
import andriy.com.aplicacion_contactos.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ContactoRepository extends JpaRepository<Contacto, Long> {

    List<Contacto> findByUsuario(Usuario usuario);

    List<Contacto> findByUsuarioAndEtiquetas_Id(Usuario usuario, Long etiqueta_id);

    Optional<Contacto> findByIdAndUsuario(Long id, Usuario usuario);

    @Modifying
    @Query(
            value = "DELETE FROM contacto_etiqueta WHERE etiqueta_id = :etiquetaId",
            nativeQuery = true
    )
    int eliminarAsociacionesEtiqueta(@Param("etiquetaId") Long etiquetaId);

}
