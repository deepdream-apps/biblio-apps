package fr.cmci.biblio.catalog.repository;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.cmci.biblio.catalog.data.Auteur;
import fr.cmci.biblio.catalog.data.Livre;
@Repository
public interface LivreRepository extends CrudRepository<Livre, Long> {
	public List<Livre> findByAuteurOrderByCategorie1(Auteur auteur) ;
}
