package fr.cmci.biblio.security.repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import fr.cmci.biblio.security.data.Evenement;
@Repository
public interface EvenementRepository extends CrudRepository<Evenement, Long>{

}
