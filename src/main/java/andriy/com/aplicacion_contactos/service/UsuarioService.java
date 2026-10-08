package andriy.com.aplicacion_contactos.service;

import andriy.com.aplicacion_contactos.dtos.UsuarioRegistroDTO;
import andriy.com.aplicacion_contactos.entities.Usuario;
import andriy.com.aplicacion_contactos.repositories.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registrarUsuario(UsuarioRegistroDTO usuario) {

        if (usuarioRepository.existsByEmail(usuario.email()))
            throw new IllegalArgumentException("El email ya existe");

        Usuario nuevoUsuario = new Usuario();

        nuevoUsuario.setEmail(usuario.email());
        nuevoUsuario.setNombre(usuario.nombre());
        nuevoUsuario.setPassword(passwordEncoder.encode(usuario.password()));

        usuarioRepository.save(nuevoUsuario);

    }

    public Usuario obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No se ha encontrado el usuario"));
    }

}
