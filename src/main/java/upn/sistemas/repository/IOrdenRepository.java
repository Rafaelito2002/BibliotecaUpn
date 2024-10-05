package upn.sistemas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import upn.sistemas.model.Orden;
import upn.sistemas.model.Usuario;

@Repository
public interface IOrdenRepository extends JpaRepository<Orden, Integer>{

	List<Orden> findByUsuario(Usuario usuario);
	
}
 