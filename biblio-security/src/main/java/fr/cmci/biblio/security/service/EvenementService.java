package fr.cmci.biblio.security.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import fr.cmci.biblio.security.dao.SequenceDAO;
import fr.cmci.biblio.security.data.Evenement;
import fr.cmci.biblio.security.enums.StatutU;
import fr.cmci.biblio.security.repository.EvenementRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.transaction.Transactional;

@Service
@Transactional
public class EvenementService {
	private Logger logger = Logger.getLogger(EvenementService.class.getName()) ;
	@Autowired
	private EvenementRepository evenementDAO ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	@Autowired
	private Environment env ;

	public Evenement creer(Evenement evenement) throws Exception {
		try{
			logger.info("Lancement de la creation d'un evenement") ;	
			evenement.setDateCreation(LocalDateTime.now());
			evenement.setDateDernMaj(LocalDateTime.now());
			evenement.setId(sequenceDAO.nextId(Evenement.class.getName())) ;
			evenementDAO.save(evenement) ;
			return evenement ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex;
		}
	}

	
	public void supprimer(Evenement evenement) throws Exception {
		try{
			logger.info("Lancement de la suppression d'un evenement");
			evenementDAO.delete(evenement) ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public Evenement rechercher(long id) throws Exception {
		try{
			logger.info("Lancement de la recherche d'un evenement");
			Evenement evenement = evenementDAO.findById(id).orElseThrow(Exception::new) ;
			return evenement ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Evenement rechercher(String email) throws Exception {
		try{
			logger.info("Lancement de la recherche d'un evenement a partir de l'email");
			Evenement evenement = null ;
			return evenement ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	public List<Evenement> rechercher(Evenement evenement) throws Exception {
		try{
			logger.info("Lancement de la recherche des evenements");
			Iterable<Evenement> evenements = evenementDAO.findAll() ;
			List<Evenement> listeEvenements = new ArrayList<Evenement>() ;
			evenements.forEach(listeEvenements::add) ;
			return listeEvenements ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}


	public Evenement modifier(Evenement evenement) throws Exception {
		try{
			logger.info("Lancement de la modification d'un evenement");
			evenement.setDateDernMaj(LocalDateTime.now());
			evenementDAO.save(evenement) ;
			return evenement ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Evenement activer(Evenement evenement) throws Exception {
		try{
			logger.info("Lancement de l'activation d'un evenement");
			
			return evenement ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public Evenement suspendre(Evenement evenement) throws Exception {
		try{
			logger.info("Lancement de la suspension d'un evenement");
			
			return evenement ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
