package fr.cmci.biblio.operations.service;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import fr.cmci.biblio.operations.data.Abonne;
import fr.cmci.biblio.operations.repository.AbonneRepository;
import fr.cmci.biblio.operations.util.CryptoSystem;
import fr.cmci.biblio.operations.util.EmailSender;

@Service
@Transactional
public class AbonneService {
	private Logger logger = Logger.getLogger(AbonneService.class.getName()) ;
	@Autowired
	private AbonneRepository abonneDAO ;
	@Autowired
	private EmailSender emailSender ;
	@Value("classpath:img/photo-empty.png")
	Resource resourceFile;
	@Autowired
	private Environment env ;

	public Abonne creer(Abonne abonne) throws Exception {
		try{
			logger.info("Lancement de la creation d'un abonne") ;	
			abonne.setDateCreation(LocalDateTime.now());
			abonne.setDateDernMaj(LocalDateTime.now());
			
			Abonne abonneCree = abonneDAO.save(abonne) ;
			Map<String, Object> params = new HashMap<String, Object>() ;
			params.put("prenom", abonne.getPrenom()) ;
			params.put("lien", "http://localhost:8080/public/confirmation-email-abonne/"+abonne.getId()) ;
			//params.put("lien", "http://localhost:8080/public/ajout-abonne-2/"+
				//		CryptoSystem.getCrypto().encrypt(Long.toString(abonne.getId()))) ;
			try {
				emailSender.sendMessage(abonne.getEmail(), "Confirmation de votre adresse e-mail", "email-templates/new-subscriber-message.html", 
					 params) ;
			}catch(Exception ex) {}
			return abonneCree ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex;
		}
	}

	
	public void supprimer(Abonne abonne) throws Exception {
		try{
			logger.info("Lancement de la suppression d'un abonne");
			abonneDAO.delete(abonne) ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public Abonne rechercher(Long id) throws Exception {
		try{
			logger.info("Lancement de la recherche d'un abonne");
			Abonne abonne = abonneDAO.findById(id).orElseThrow(Exception::new) ;
			return abonne ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public Abonne rechercher(String email) throws Exception {
		try{
			logger.info("Lancement de la recherche d'un abonne");
			List<Abonne> abonnes = abonneDAO.findByEmail(email) ;
			return abonnes.size() == 0 ? null : abonnes.get(0) ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	

	public List<Abonne> rechercher(Abonne abonne) throws Exception {
		try{
			logger.info("Lancement de la recherche des abonnes");
			Iterable<Abonne> abonnes = abonneDAO.findAll() ;
			List<Abonne> listeAbonnes = new ArrayList<Abonne>() ;
			abonnes.forEach(listeAbonnes::add) ;
			return listeAbonnes ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}


	public Abonne modifier(Abonne abonne) throws Exception {
		try{
			logger.info("Lancement de la modification d'un abonne");
			abonne.setDateDernMaj(LocalDateTime.now());
			abonneDAO.save(abonne) ;
			return abonne ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
}
