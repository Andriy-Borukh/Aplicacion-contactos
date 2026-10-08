package andriy.com.aplicacion_contactos.controller;

import andriy.com.aplicacion_contactos.dtos.UsuarioRegistroDTO;
import andriy.com.aplicacion_contactos.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {

        model.addAttribute("usuario", new UsuarioRegistroDTO(
                "",
                "",
                ""
        ));

        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(
            @Valid @ModelAttribute("usuario") UsuarioRegistroDTO usuario,
            BindingResult result
    ) {

        if (result.hasErrors()) {
            return "registro";
        }

        try {
            usuarioService.registrarUsuario(usuario);
        } catch (IllegalArgumentException e) {
            result.rejectValue("email", "email.duplicado", e.getMessage());
            return "registro";
        }

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        return "login";
    }

}
