package fr.cmci.biblio.operations.repository;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import fr.cmci.biblio.operations.data.Abonne;
@Repository
public interface AbonneRepository extends CrudRepository<Abonne, Long> {
	public List<Abonne> findByEmail (String email) ;
}
