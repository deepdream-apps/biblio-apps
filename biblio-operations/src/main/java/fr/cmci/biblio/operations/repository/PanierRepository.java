package fr.cmci.biblio.operations.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.cmci.biblio.operations.data.Abonne;
import fr.cmci.biblio.operations.data.Panier;
@Repository
public interface PanierRepository extends CrudRepository<Panier, Long> {
	public Panier findByAbonne(Abonne abonne) ;
}
