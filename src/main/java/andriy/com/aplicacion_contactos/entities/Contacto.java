package andriy.com.aplicacion_contactos.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Contacto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "El nombre es obligatorio")
    String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    String apellidos;

    @NotBlank(message = "El telefono es obligatorio")
    @Pattern(
            regexp = "^\\+[1-9]\\d{1,14}$",
            message = "El telefono no es valido"
    )
    String telefono;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no es valido")
    String email;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    Usuario usuario;

}
