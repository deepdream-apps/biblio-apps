package fr.cmci.biblio.operations.service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.cmci.biblio.catalog.data.Livre;
import fr.cmci.biblio.operations.data.Abonne;
import fr.cmci.biblio.operations.data.DetailPret;
import fr.cmci.biblio.operations.data.Panier;
import fr.cmci.biblio.operations.data.Pret;
import fr.cmci.biblio.operations.enums.StatutP;
import fr.cmci.biblio.operations.repository.DetailPretRepository;
import fr.cmci.biblio.operations.repository.PanierRepository;
import fr.cmci.biblio.operations.repository.PretRepository;
import fr.cmci.biblio.operations.util.EmailSender;
import freemarker.template.Configuration;

@Service
@Transactional
public class PretService {
	private Logger logger = Logger.getLogger(PretService.class.getName()) ;
	@Autowired
	private PretRepository pretDAO ;
	@Autowired
	private DetailPretRepository detailRepository ;
	@Autowired
	private PanierRepository panierRepository ;
	@Autowired
	private EmailSender emailSender ;
	@Autowired
	private DetailPretRepository detailPretRepository ;
	
	@Autowired
	private Environment env ;

	public Pret creer(Pret pret) throws Exception {
		try{
			logger.info("Lancement de la creation d'un pret") ;	
			pret.setDateCreation(LocalDateTime.now());
			pret.setDateDernMaj(LocalDateTime.now());

			Pret pretCree = pretDAO.save(pret) ;
			
			return pretCree ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex;
		}
	}
	
	
	public Pret creer(Panier panier) throws Exception {
		try{
			logger.info("Lancement de la creation d'un pret (panier)") ;	
			Pret pret = new Pret() ;
			pret.setDateSoumission(LocalDateTime.now());
			  
		    pret.setNbLivres(panier.getListeLivres().size());
		    
		    pret.setAbonne(panier.getAbonne());
			  
			pret.setDateCreation(LocalDateTime.now());
			pret.setDateDernMaj(LocalDateTime.now());
			
			pret.setStatut(StatutP.En_Attente.toString());
			
			pret.setNbLivres(panier.getListeLivres().size());

			Pret pretCree = pretDAO.save(pret) ;
			
			panier.getListeLivres().forEach(livre -> {
				  DetailPret detail = new DetailPret() ;
				  detail.setPret(pretCree);
				  detail.setLivre(livre);
				  detail.setQuantite(1);
				  detail.setStatut(StatutP.En_Attente.toString());
				  detail.setDateCreation(LocalDateTime.now());
				  detail.setDateDernMaj(LocalDateTime.now());
				  detailRepository.save(detail) ;
			  });
		    
			List<Livre> listeLivres = new ArrayList<Livre>() ;
			
			panier.getListeLivres().forEach(listeLivres::add);
			
			panier.getListeLivres().removeIf(livre -> {return true;});
			
			panierRepository.save(panier) ;
			
			Map<String, Object> params = new HashMap<String, Object>() ;
			
			Abonne abonne = pret.getAbonne() ;
			
			logger.log(Level.INFO, "Nombre de livres commandés "+listeLivres.size());
			
			params.put("prenom", abonne.getPrenom()) ;
			params.put("listeLivres", listeLivres) ;
			
			try {
				emailSender.sendMessage(abonne.getEmail(), "Votre demande de prêt", "email-templates/nouvelle-demande-pret.html", 
					 params) ;
			}catch(Exception ex) {
				logger.log(Level.SEVERE, ex.getMessage(), ex);
			}
		
			return pretCree ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex;
		}
	}

	
	public Pret accorder(Pret pret) throws Exception {
		try{
			logger.info("Lancement de l'accord d'un pret") ;	
			pret.setDateDernMaj(LocalDateTime.now());
			pret.setDateTraitement(LocalDateTime.now());
			pret.setStatut(StatutP.Accepte.toString());
			Pret pretAccorde = pretDAO.save(pret) ;
			
			List<DetailPret> listeDetails = detailPretRepository.findByPret(pret) ;
			
			List<Livre> listeLivres = listeDetails.stream().map(DetailPret::getLivre).collect(Collectors.toList()) ;
			
			Map<String, Object> params = new HashMap<String, Object>() ;
			
			Abonne abonne = pret.getAbonne() ;
			
			logger.log(Level.INFO, "Nombre de livres acceptés "+listeLivres.size());
			
			params.put("abonne", abonne) ;
			params.put("listeLivres", listeLivres) ;
			
			try {
				emailSender.sendMessage(abonne.getEmail(), "Acceptation de votre demande de prêt", "email-templates/demande-pret-acceptee.html", 
					 params) ;
			}catch(Exception ex) {
				logger.log(Level.SEVERE, ex.getMessage(), ex);
			}
			return pretAccorde ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex;
		}
	}
	
	
	public Pret refuser(Pret pret) throws Exception {
		try{
			logger.info("Lancement du refus d'un pret") ;	
			pret.setDateDernMaj(LocalDateTime.now());
			pret.setDateTraitement(LocalDateTime.now());
			pret.setStatut(StatutP.Refuse.toString());
			Pret pretRefuse = pretDAO.save(pret) ;
			
			List<DetailPret> listeDetails = detailPretRepository.findByPret(pret) ;
			
			List<Livre> listeLivres = listeDetails.stream().map(DetailPret::getLivre).collect(Collectors.toList()) ;
			
			Map<String, Object> params = new HashMap<String, Object>() ;
			
			Abonne abonne = pret.getAbonne() ;
			
			logger.log(Level.INFO, "Nombre de livres refusés "+listeLivres.size());
			
			params.put("abonne", abonne) ;
			params.put("listeLivres", listeLivres) ;
			
			try {
				emailSender.sendMessage(abonne.getEmail(), "Refus de votre demande de prêt", "email-templates/demande-pret-refusee.html", 
					 params) ;
			}catch(Exception ex) {
				logger.log(Level.SEVERE, ex.getMessage(), ex);
			}
			return pretRefuse ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex;
		}
	}
	
	
	public Pret validerEnvoi (Pret pret) throws Exception {
		try{
			logger.info("Lancement du refus d'une validation d'envoi") ;	
			pret.setDateDernMaj(LocalDateTime.now());
			pret.setDateTransmission(LocalDateTime.now());
			pret.setStatut(StatutP.Achemine.toString());
			Pret pretAchemine = pretDAO.save(pret) ;
			
			List<DetailPret> listeDetails = detailPretRepository.findByPret(pret) ;
			
			List<Livre> listeLivres = listeDetails.stream().map(DetailPret::getLivre).collect(Collectors.toList()) ;
			
			Map<String, Object> params = new HashMap<String, Object>() ;
			
			Abonne abonne = pret.getAbonne() ;
			
			logger.log(Level.INFO, "Nombre de livres acheminés "+listeLivres.size());
			
			params.put("abonne", abonne) ;
			params.put("listeLivres", listeLivres) ;
			
			try {
				emailSender.sendMessage(abonne.getEmail(), "Acheminement de votre demande de prêt", "email-templates/demande-pret-acheminee.html", 
					 params) ;
			}catch(Exception ex) {
				logger.log(Level.SEVERE, ex.getMessage(), ex);
			}
			return pretAchemine ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex;
		}
	}
	
	
	
	public void supprimer(Pret pret) throws Exception {
		try{
			logger.info("Lancement de la suppression d'un pret");
			pretDAO.delete(pret) ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public Pret rechercher(Long id) throws Exception {
		try{
			logger.info("Lancement de la recherche d'un pret");
			Pret pret = pretDAO.findById(id).orElseThrow(Exception::new) ;
			return pret ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Pret> rechercherStatut(String statut) throws Exception {
		try{
			logger.info("Lancement de la recherche des prets par statut");
			List<Pret> listeEmprunts = pretDAO.findByStatut(statut) ;
			return listeEmprunts ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	

	public List<Pret> rechercher(Pret pret) throws Exception {
		try{
			logger.info("Lancement de la recherche des prets");
			Iterable<Pret> prets = pretDAO.findAll() ;
			List<Pret> listeEmprunts = new ArrayList<Pret>() ;
			prets.forEach(listeEmprunts::add) ;
			return listeEmprunts ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}


	public Pret modifier(Pret pret) throws Exception {
		try{
			logger.info("Lancement de la modification d'un pret");
			pret.setDateDernMaj(LocalDateTime.now());
			pretDAO.save(pret) ;
			return pret ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
}
