package fr.cmci.biblio.catalog.service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import fr.cmci.biblio.catalog.data.Auteur;
import fr.cmci.biblio.catalog.data.Categorie;
import fr.cmci.biblio.catalog.repository.CategorieRepository;

@Service
@Transactional
public class CategorieService {
	private Logger logger = Logger.getLogger(CategorieService.class.getName()) ;
	@Autowired
	private CategorieRepository categorieDAO ;

	@Autowired
	private Environment env ;

	public Categorie creer(Categorie categorie) throws Exception {
		try{
			logger.info("Lancement de la creation d'un categorie") ;
			categorie.setDateCreation(LocalDateTime.now());
			categorie.setDateDernMaj(LocalDateTime.now());
			categorieDAO.save(categorie) ;
			return categorie ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex;
		}
	}

	
	public void supprimer(Categorie categorie) throws Exception {
		try{
			logger.info("Lancement de la suppression d'une categorie");
			categorieDAO.delete(categorie) ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public Categorie rechercher(Long id) throws Exception {
		try{
			logger.info("Lancement de la recherche d'une categorie");
			Categorie categorie = categorieDAO.findById(id).orElseThrow(Exception::new) ;
			return categorie ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	

	public List<Categorie> rechercher(Categorie categorie) throws Exception {
		try{
			logger.info("Lancement de la recherche des categories");
			Iterable<Categorie> categories = categorieDAO.findAll() ;
			List<Categorie> listeCategories = new ArrayList<Categorie>() ;
			categories.forEach(listeCategories::add) ;
			return listeCategories ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}


	public Categorie modifier(Categorie categorie) throws Exception {
		try{
			logger.info("Lancement de la modification d'une categorie");
			categorie.setDateDernMaj(LocalDateTime.now());
			categorieDAO.save(categorie) ;
			return categorie ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
}
