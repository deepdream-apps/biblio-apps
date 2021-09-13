package fr.cmci.biblio.catalog.repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import fr.cmci.biblio.catalog.data.Categorie;
@Repository
public interface CategorieRepository extends CrudRepository<Categorie, Long>{

}
