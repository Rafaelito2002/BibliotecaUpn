package upn.sistemas.service;

import java.util.List;
import java.util.Optional;

import upn.sistemas.model.Orden;
import upn.sistemas.model.Usuario;

public interface IOrdenService {
	List<Orden> findAll();
	Optional<Orden> findById(Integer id);
	
	Orden save (Orden orden);
	String generarNumeroOrden();
	List<Orden> findByUsuario(Usuario usuario);

}
