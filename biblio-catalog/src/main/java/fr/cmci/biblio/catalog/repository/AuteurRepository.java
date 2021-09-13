package fr.cmci.biblio.catalog.repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import fr.cmci.biblio.catalog.data.Auteur;
@Repository
public interface AuteurRepository extends CrudRepository<Auteur, Long>{
	
}
