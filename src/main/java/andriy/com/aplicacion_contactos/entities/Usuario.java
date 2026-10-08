package andriy.com.aplicacion_contactos.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank
    String nombre;

    @Column(unique = true, nullable = false)
    String email;

    @Column(nullable = false)
    String password;

    LocalDate fechaRegistro;

    @PrePersist
    public void prePersist() {
        fechaRegistro = LocalDate.now();
    }

}
