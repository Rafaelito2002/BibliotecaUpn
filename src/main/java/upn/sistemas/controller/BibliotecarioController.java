package upn.sistemas.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import upn.sistemas.model.Orden;
import upn.sistemas.model.Producto;
import upn.sistemas.service.IOrdenService;
import upn.sistemas.service.ProductoService;

@Controller
@RequestMapping("/bibliotecario")
public class BibliotecarioController {
	@Autowired
	private ProductoService productoService;
	
	
	@Autowired
	private IOrdenService ordenService;
	
	private Logger logg = LoggerFactory.getLogger(AdministradorController.class);
	
	@GetMapping("")
	public String home(Model model) {
		List<Producto>productos=productoService.findAll();
		model.addAttribute("productos",productos);
		return"bibliotecario/home";
	}
	
	
	
	@GetMapping("/ordenes")
	public String ordernes(Model model) {
		model.addAttribute("ordenes", ordenService.findAll());
		return "bibliotecario/ordenes";
	}
	
	@GetMapping("/detalle/{id}")
	public String detalle(Model model, @PathVariable Integer id) {
		logg.info("Id de la orden {}",id);
		Orden orden = ordenService.findById(id).get();
		
		model.addAttribute("detalles", orden.getDetalle());
		
		return "bibliotecario/detalleorden";
	}


}
