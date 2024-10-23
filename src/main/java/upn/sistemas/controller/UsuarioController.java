package upn.sistemas.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<?> save(Usuario usuario, Model model) {
		logger.info("Usuario registro: {}", usuario);
		
		// Validaciones de nombre
		String nombre = usuario.getNombre();
		if (nombre == null || nombre.isEmpty()) {
			return ResponseEntity.badRequest().body("Por favor, complete estos campos.");
		}
		
		if (nombre.trim().length() < 3) {
			return ResponseEntity.badRequest().body("Escriba un nombre válido");
		}
		
		if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
			return ResponseEntity.badRequest().body("El campo Nombres contiene caracteres no permitidos.");
		}
		
		if (nombre.contains("  ")) {
			return ResponseEntity.badRequest().body("No espacios en blanco, por favor.");
		}

		// Validaciones de contraseña
		String password = usuario.getPassword();
		if (password == null || password.isEmpty()) {
			return ResponseEntity.badRequest().body("Por favor, complete estos campos.");
		}

		if (password.length() < 8) {
			return ResponseEntity.badRequest().body("La contraseña debe tener al menos 8 caracteres.");
		}

		if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
			return ResponseEntity.badRequest().body("La contraseña debe incluir al menos un carácter especial.");
		}

		if (!password.matches(".*[A-Z].*")) {
			return ResponseEntity.badRequest().body("La contraseña debe incluir al menos una letra mayúscula.");
		}

		if (!password.matches(".*\\d.*")) {
			return ResponseEntity.badRequest().body("La contraseña debe incluir al menos un número.");
		}

		// Si todas las validaciones son correctas
		usuario.setTipo("USER");
		usuarioService.save(usuario);
		
		// Creamos la respuesta
		Map<String, String> response = new HashMap<>();
		response.put("status", "success");
		response.put("message", "Usuario registrado exitosamente");
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/login")
	public String login() {
		return "usuario/login";
	}
	
	@PostMapping("/acceder")
	public ResponseEntity<?> acceder(Usuario usuario, HttpSession session) {
		logger.info("Accesos : {}", usuario);
		Optional<Usuario> user = usuarioService.findByEmail(usuario.getEmail());
		
		if(user.isPresent()) {
			session.setAttribute("idusuario", user.get().getId());
			
			// Creamos un nombre completo combinando nombre y apellidos
			String nombreCompleto = user.get().getNombre();
			if (user.get().getApellidos() != null && !user.get().getApellidos().isEmpty()) {
				nombreCompleto += " " + user.get().getApellidos();
			}
			
			// Determinamos la URL de redirección según el tipo de usuario
			String redirectUrl;
			if(user.get().getTipo().equals("ADMIN")) {
				redirectUrl = "/administrador";
			} else if(user.get().getTipo().equals("BIBLIOTECARIO")) {
				redirectUrl = "/bibliotecario";
			} else {
				redirectUrl = "/";
			}
			
			// Creamos la respuesta con el nombre completo y la URL de redirección
			Map<String, String> response = new HashMap<>();
			response.put("nombreCompleto", nombreCompleto);
			response.put("redirect", redirectUrl);
			
			return ResponseEntity.ok().body(response);
		} else {
			logger.info("Usuario no existe");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
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
