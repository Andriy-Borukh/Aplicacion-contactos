package andriy.com.aplicacion_contactos.controller;

import andriy.com.aplicacion_contactos.dtos.ContactoRegistroDTO;
import andriy.com.aplicacion_contactos.dtos.ContactoResponseDTO;
import andriy.com.aplicacion_contactos.entities.Usuario;
import andriy.com.aplicacion_contactos.service.ContactoService;
import andriy.com.aplicacion_contactos.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ContactoController {

    private final ContactoService contactoService;
    private final UsuarioService usuarioService;

    public ContactoController(ContactoService contactoService,
                              UsuarioService usuarioService) {
        this.contactoService = contactoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/contactos")
    public String contactos(Authentication authentication, Model model) {
        String email = authentication.getName();

        Usuario usuario = usuarioService.obtenerPorEmail(email);

        model.addAttribute(
                "contactos",
                contactoService.obtenerContactos(usuario)
        );

        return "contactos";
    }

    @GetMapping("/contactos/nuevo")
    public String mostrarFormulario(Model model) {

        model.addAttribute(
                "contacto",
                new ContactoRegistroDTO("", "", "", "")
        );

        model.addAttribute("accion", "/contactos");

        return "contacto-form";
    }

    @PostMapping("/contactos")
    public String guardarContacto(
            @Valid @ModelAttribute("contacto") ContactoRegistroDTO contacto,
            BindingResult result,
            Authentication authentication
    ) {

        if (result.hasErrors()) {
            return "contacto-form";
        }

        Usuario usuario = usuarioService.obtenerPorEmail(authentication.getName());

        contactoService.guardarContacto(contacto, usuario);

        return "redirect:/contactos";
    }

    @GetMapping("/contactos/{id}/editar")
    public String mostrarFormularioEditar(
            @PathVariable Long id,
            Authentication authentication,
            Model model
    ) {
        Usuario usuario = usuarioService.obtenerPorEmail(authentication.getName());

        ContactoResponseDTO contacto = contactoService.obtenerContacto(id, usuario);

        model.addAttribute(
                "contacto",
                new ContactoRegistroDTO(
                        contacto.nombre(),
                        contacto.apellidos(),
                        contacto.telefono(),
                        contacto.email()
                )
        );

        model.addAttribute("accion", "/contactos/" + id);

        return "contacto-form";
    }

    @PostMapping("/contactos/{id}")
    public String editarContacto(
            @PathVariable Long id,
            @Valid @ModelAttribute("contacto") ContactoRegistroDTO contacto,
            BindingResult result,
            Authentication authentication,
            Model model
    ) {

        if (result.hasErrors()) {
            model.addAttribute("accion", "/contactos/" + id);
            return "contacto-form";
        }

        Usuario usuario = usuarioService.obtenerPorEmail(authentication.getName());

        contactoService.editarContacto(id, contacto, usuario);

        return "redirect:/contactos";
    }

    @PostMapping("/contactos/{id}/eliminar")
    public String eliminarContacto(
            @PathVariable Long id,
            Authentication authentication
    ) {
        Usuario usuario = usuarioService.obtenerPorEmail(authentication.getName());

        contactoService.eliminarContacto(id, usuario);

        return "redirect:/contactos";
    }

}
