package fr.cmci.biblio.security.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import fr.cmci.biblio.security.util.CryptoSystem;
import fr.cmci.biblio.security.util.EmailSender;
import fr.cmci.biblio.security.dao.SequenceDAO;
import fr.cmci.biblio.security.data.Utilisateur;
import fr.cmci.biblio.security.enums.RoleU;
import fr.cmci.biblio.security.enums.StatutU;
import fr.cmci.biblio.security.repository.UtilisateurRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.transaction.Transactional;

@Service
@Transactional
public class UtilisateurService {
	private Logger logger = Logger.getLogger(UtilisateurService.class.getName()) ;
	@Autowired
	private UtilisateurRepository utilisateurDAO ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	@Autowired
	private EmailSender emailSender ;
	@Autowired
	private CryptoSystem cryptoSystem ;
	@Autowired
	private Environment env ;

	public Utilisateur creer(Utilisateur utilisateur) throws Exception {
		try{
			logger.info("Lancement de la creation d'un utilisateur") ;	
			utilisateur.setDateCreation(LocalDateTime.now());
			utilisateur.setDateDernMaj(LocalDateTime.now());
			utilisateur.setDateExpirationMdp(LocalDateTime.now().plusMonths(Long.parseLong(env.getProperty("app.security.password_validity_period")))) ;
			utilisateur.setDateExpiration(LocalDateTime.now().plusMonths(Long.parseLong(env.getProperty("app.security.account_validity_period")))) ;
			utilisateur.setId(sequenceDAO.nextId(Utilisateur.class.getName())) ;
			utilisateur.setLibelleRole(RoleU.getLibelle(utilisateur.getRole()));
			if(utilisateur.getStatut() == null )
				utilisateur.setStatut(StatutU.Suspendu.getId());
			utilisateur.setLibelleStatut(StatutU.getLibelle(utilisateur.getStatut()));
			Utilisateur utilisateurCree = utilisateurDAO.save(utilisateur) ;
			
			Map<String, Object> params = new HashMap<String, Object>() ;
			params.put("prenom", utilisateurCree.getPrenom()) ;
			params.put("lien", env.getProperty("app.biblio.web.url")+"/public/confirmation-utilisateur/"+utilisateurCree.getId()) ;
			//params.put("lien", env.getProperty("app.biblio.web.url")+"/public/confirmation-utilisateur/"+cryptoSystem.encrypt(Long.toString(utilisateurCree.getId()))) ;
			try {
				emailSender.sendMessage(utilisateurCree.getEmail(), "Votre compte utilisateur", "new-user-message.html", 
					 params) ;
			}catch(Exception ex) {}

			return utilisateur ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex;
		}
	}

	
	public void supprimer(Utilisateur utilisateur) throws Exception {
		try{
			logger.info("Lancement de la suppression d'un utilisateur");
			utilisateurDAO.delete(utilisateur) ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public Utilisateur rechercher(long id) throws Exception {
		try{
			logger.info("Lancement de la recherche d'un utilisateur");
			Utilisateur utilisateur = utilisateurDAO.findById(id).orElseThrow(Exception::new) ;
			return utilisateur ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Utilisateur rechercher(String email) throws Exception {
		try{
			logger.info("Lancement de la recherche d'un utilisateur a partir de l'email");
			Utilisateur utilisateur = utilisateurDAO.findByEmail(email);
			return utilisateur ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	public List<Utilisateur> rechercher(Utilisateur utilisateur) throws Exception {
		try{
			logger.info("Lancement de la recherche des utilisateurs");
			Iterable<Utilisateur> utilisateurs = utilisateurDAO.findAll() ;
			List<Utilisateur> listeUtilisateurs = new ArrayList<Utilisateur>() ;
			utilisateurs.forEach(listeUtilisateurs::add) ;
			return listeUtilisateurs ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}


	public Utilisateur modifier(Utilisateur utilisateur) throws Exception {
		try{
			logger.info("Lancement de la modification d'un utilisateur");
			utilisateur.setDateDernMaj(LocalDateTime.now());
			utilisateur.setLibelleRole(RoleU.getLibelle(utilisateur.getRole()));
			utilisateur.setLibelleStatut(StatutU.getLibelle(utilisateur.getStatut()));
			utilisateurDAO.save(utilisateur) ;
			return utilisateur ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public Utilisateur confirmer(Utilisateur utilisateur) throws Exception {
		try{
			logger.info("Lancement de la modification d'un utilisateur");
			utilisateur.setDateDernMaj(LocalDateTime.now());
			utilisateur.setStatut(StatutU.Valide.getId());
			utilisateur.setLibelleStatut(StatutU.getLibelle(utilisateur.getStatut()));
			utilisateurDAO.save(utilisateur) ;
			return utilisateur ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Utilisateur activer(Utilisateur utilisateur) throws Exception {
		try{
			logger.info("Lancement de l'activation d'un utilisateur");
			if( StatutU.Valide.getId() != utilisateur.getStatut()) {
				utilisateur.setStatut(1);
				utilisateur.setDateExpiration(utilisateur.getDateExpiration().plusMonths(Integer.parseInt(env.getProperty("app.security.account_validity_period"))));
				utilisateurDAO.save(utilisateur) ;
			} else {
				throw new SecurityException("msg_compte_deja_active") ;
			}
			return utilisateur ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public Utilisateur suspendre(Utilisateur utilisateur) throws Exception {
		try{
			logger.info("Lancement de la suspension d'un utilisateur");
			if( StatutU.Suspendu.getId() != utilisateur.getStatut()) {
				utilisateur.setStatut(4);
				utilisateur.setDateExpiration(utilisateur.getDateExpiration().plusMonths(Integer.parseInt(env.getProperty("app.security.account_validity_period"))));
				utilisateurDAO.save(utilisateur) ;
			} else {
				throw new SecurityException("msg_compte_deja_active") ;
			}
			return utilisateur ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
