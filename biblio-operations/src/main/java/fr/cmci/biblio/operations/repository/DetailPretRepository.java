package fr.cmci.biblio.operations.repository;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import fr.cmci.biblio.operations.data.DetailPret;
import fr.cmci.biblio.operations.data.Pret;
@Repository
public interface DetailPretRepository extends CrudRepository<DetailPret, Long>{
	public List<DetailPret> findByPret (Pret pret) ;
}
