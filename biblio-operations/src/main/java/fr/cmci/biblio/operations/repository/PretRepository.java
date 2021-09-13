package fr.cmci.biblio.operations.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import fr.cmci.biblio.operations.data.Pret;

@Repository
public interface PretRepository extends CrudRepository<Pret, Long>{
	public List<Pret> findByStatut (String statut) ;
}
