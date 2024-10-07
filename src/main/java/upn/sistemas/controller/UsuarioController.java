package upn.sistemas.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import upn.sistemas.model.Orden;
import upn.sistemas.model.Usuario;
import upn.sistemas.service.IOrdenService;
import upn.sistemas.service.IUsuarioService;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
	
	private final Logger logger=LoggerFactory.getLogger(UsuarioController.class);
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private IOrdenService ordenService;
	
	// /usuario/registro
	@GetMapping("/registro")
	public String create() {
		return "usuario/registro";
	}
	
	@PostMapping("/save")
public String save(Usuario usuario, Model model) {
    logger.info("Usuario registro: {}", usuario);
    
    // Validaciones de nombre
    String nombre = usuario.getNombre();
    if (nombre == null || nombre.isEmpty()) {
        model.addAttribute("error", "Por favor, complete estos campos.");
        return "usuario/registro"; // Redirige de vuelta al formulario
    }
    
    if (nombre.trim().length() < 3) {
        model.addAttribute("error", "Escriba un nombre válido");
        return "usuario/registro";
    }
    
    if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
        model.addAttribute("error", "El campo Nombres contiene caracteres no permitidos.");
        return "usuario/registro";
    }
    
    if (nombre.contains("  ")) {
        model.addAttribute("error", "No espacios en blanco, por favor.");
        return "usuario/registro";
    }

    // Validaciones de contraseña
    String password = usuario.getPassword();
    if (password == null || password.isEmpty()) {
        model.addAttribute("error", "Por favor, complete estos campos.");
        return "usuario/registro"; // Redirige de vuelta al formulario
    }

    if (password.length() < 8) {
        model.addAttribute("error", "La contraseña debe tener al menos 8 caracteres.");
        return "usuario/registro";
    }

    if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
        model.addAttribute("error", "La contraseña debe incluir al menos un carácter especial.");
        return "usuario/registro";
    }

    if (!password.matches(".*[A-Z].*")) {
        model.addAttribute("error", "La contraseña debe incluir al menos una letra mayúscula.");
        return "usuario/registro";
    }

    if (!password.matches(".*\\d.*")) {
        model.addAttribute("error", "La contraseña debe incluir al menos un número.");
        return "usuario/registro";
    }

    // Si todas las validaciones son correctas
    usuario.setTipo("USER");
    usuarioService.save(usuario);
    return "redirect:/";
}


	
	@GetMapping("/login")
	public String login() {
		return "usuario/login";
	}
	
	@PostMapping("/acceder")
	public String acceder(Usuario usuario, HttpSession session) {
		logger.info("Accesos : {}", usuario);
		Optional<Usuario> user=usuarioService.findByEmail(usuario.getEmail());
		//logger.info("Usuario de db: {}", user.get());
		if(user.isPresent()) {
			session.setAttribute("idusuario", user.get().getId());
			if(user.get().getTipo().equals("ADMIN")) {
				return "redirect:/administrador";
			}else if(user.get().getTipo().equals("BIBLIOTECARIO")) {
					return "redirect:/bibliotecario";
				}else {
					return "redirect:/";
				}
			
		}else {
			logger.info("Usuario no existe");
			return "redirect:/";
		}
	}
	
	@GetMapping("/compras")
	public String obtenerCompras(Model model,HttpSession session) {
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		Usuario usuario= usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		List<Orden> ordenes = ordenService.findByUsuario(usuario);
		
		logger.info("ordenes {}", ordenes);
		model.addAttribute("ordenes", ordenes);
		return "usuario/compras";
	}
	
	@GetMapping("/detalle/{id}")
	public String detallecompra(@PathVariable Integer id, HttpSession session, Model model) {
		logger.info("Id de la orden: {}", id);
		Optional<Orden> orden= ordenService.findById(id);
		
		model.addAttribute("detalles", orden.get().getDetalle());
		//session
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		return "usuario/detallecompra";
	}
	
	@GetMapping("/cerrar")
	public String cerrarSesion( HttpSession session) {
		session.removeAttribute("idusuario");
		return "redirect:/";
	}

}
