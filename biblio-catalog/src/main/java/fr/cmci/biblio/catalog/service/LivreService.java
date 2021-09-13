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
import fr.cmci.biblio.catalog.data.Livre;
import fr.cmci.biblio.catalog.repository.LivreRepository;

@Service
@Transactional
public class LivreService {
	private Logger logger = Logger.getLogger(LivreService.class.getName()) ;
	@Autowired
	private LivreRepository livreDAO ;
	@Autowired
	private Environment env ;
	

	public Livre creer(Livre livre) throws Exception {
		try{
			logger.info("Lancement de la creation d'un livre") ;	
			livre.setDateCreation(LocalDateTime.now());
			livre.setDateDernMaj(LocalDateTime.now());
			livreDAO.save(livre) ;
			return livre ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex;
		}
	}

	
	public void supprimer(Livre livre) throws Exception {
		try{
			logger.info("Lancement de la suppression d'un livre");
			livreDAO.delete(livre) ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public Livre rechercher(Long id) throws Exception {
		try{
			logger.info("Lancement de la recherche d'un livre");
			Livre livre = livreDAO.findById(id).orElseThrow(Exception::new) ;
			return livre ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	

	public List<Livre> rechercher(Livre livre) throws Exception {
		try{
			logger.info("Lancement de la recherche des livres");
			Iterable<Livre> livres = livreDAO.findAll() ;
			List<Livre> listeLivres = new ArrayList<Livre>() ;
			livres.forEach(listeLivres::add) ;
			return listeLivres ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public List<Livre> rechercher(Auteur auteur) throws Exception {
		try{
			logger.info("Lancement de la recherche des livres d'un auteur");
			List<Livre> listeLivres = livreDAO.findByAuteurOrderByCategorie1(auteur);
			return listeLivres ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}


	public Livre modifier(Livre livre) throws Exception {
		try{
			logger.info("Lancement de la modification d'un livre");
			livre.setDateDernMaj(LocalDateTime.now());
			livreDAO.save(livre) ;
			return livre ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
}
