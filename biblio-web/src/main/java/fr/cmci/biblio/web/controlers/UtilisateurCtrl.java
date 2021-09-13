package fr.cmci.biblio.web.controlers;
import java.io.Serializable;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;

import fr.cmci.biblio.operations.data.Abonne;
import fr.cmci.biblio.security.data.Utilisateur;
import fr.cmci.biblio.security.enums.RoleU;
import fr.cmci.biblio.security.enums.StatutU;

@Controller
@SessionAttributes({"utilisateurCourant"})
public class UtilisateurCtrl implements Serializable{
	private Logger logger = Logger.getLogger(UtilisateurCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;	
	@Autowired
	private Environment env ;
	@Autowired
	private PasswordEncoder passwordEncoder ;
	@Autowired
	private AuthenticationManager  authenticationManager ;

	
	@GetMapping ("/private/admin/utilisateurs")
	public String index (Model model) throws Exception {
		 ResponseEntity<Utilisateur[]> response = rest.getForEntity("http://biblio-zuul/biblio-security/ws/utilisateur/all", Utilisateur[].class) ;
		 List<Utilisateur> listeUtilisateurs = Arrays.asList(response.getBody());
		 model.addAttribute("listeUtilisateurs", listeUtilisateurs) ;
		return "private/utilisateurs" ;
	}
	
	@GetMapping ("/private/admin/ajout-utilisateur")
	public String initAjout (Model model) throws Exception {
		model.addAttribute("utilisateur", new Utilisateur()) ;
		return "private/ajout-utilisateur" ;
	}
	
	@PostMapping ("/private/admin/utilisateur/ajout")
	public String ajouter (Model model, @RequestParam("nom") String nom,
			@RequestParam("prenom") String prenom,
			@RequestParam("email") String email, 
			@RequestParam("telephone") String telephone, 
			@RequestParam("pays") String pays, 
			@RequestParam("role") Integer role,
			Principal principal) throws Exception {
		try {
			
			Utilisateur utilisateur = new Utilisateur() ;
			
			utilisateur.setCreateur(principal.getName());
			utilisateur.setModificateur(principal.getName());
			
			utilisateur.setNom(nom);
			utilisateur.setPrenom(prenom);
			utilisateur.setEmail(email);
			utilisateur.setTelephone(telephone);
			utilisateur.setPays(pays);
			utilisateur.setRole(role);
			
			ResponseEntity<Utilisateur> response = rest.postForEntity("http://biblio-zuul/biblio-security/ws/utilisateur/ajout", utilisateur, Utilisateur.class) ;		  
			//model.addAttribute("listeUtilisateurs", listeUtilisateurs) ;
		    return "redirect:/private/admin/utilisateurs" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			return "private/ajout-utilisateur" ;
		}
	}
	
	@GetMapping ("/private/admin/maj-utilisateur/{id}")
	public String initMaj (Model model, @PathVariable("id")Long id) throws Exception {
		Utilisateur utilisateur = rest.getForObject("http://biblio-zuul/biblio-security/ws/utilisateur/id/{id}", Utilisateur.class, id) ;		  
		model.addAttribute("utilisateurExistant", utilisateur) ;model.addAttribute("utilisateurExistant", utilisateur) ;
		return "private/maj-utilisateur" ;
	}
	
	@PostMapping ("/private/admin/utilisateur/maj")
	public String modifier (Model model, Utilisateur utilisateurExistant, Principal principal) throws Exception {
		try {
			
			Utilisateur utilisateur = rest.getForObject("http://biblio-zuul/biblio-security/ws/utilisateur/id/{id}", Utilisateur.class, utilisateurExistant.getId()) ;		  
			
			utilisateur.setCreateur(principal.getName());
			utilisateur.setModificateur(principal.getName());
			
			utilisateur.setNom(utilisateurExistant.getNom());
			utilisateur.setPrenom(utilisateurExistant.getPrenom());
			utilisateur.setEmail(utilisateurExistant.getEmail());
			utilisateur.setTelephone(utilisateurExistant.getTelephone());
			utilisateur.setRole(utilisateurExistant.getRole());
			utilisateur.setStatut(utilisateurExistant.getStatut());
			utilisateur.setPays(utilisateurExistant.getPays());
			
			ResponseEntity<Utilisateur> response = rest.postForEntity("http://biblio-zuul/biblio-security/ws/utilisateur/maj", utilisateur, Utilisateur.class) ;		  
			//model.addAttribute("listeUtilisateurs", listeUtilisateurs) ;
			return "redirect:/private/admin/utilisateurs" ;
		}catch(Exception ex) {
			 logger.log(Level.SEVERE, ex.getMessage());
			 model.addAttribute("utilisateurExistant", utilisateurExistant) ;
			 return "redirect:/private/maj-utilisateur" ;
		}
	}
	
	
	@GetMapping ("/public/confirmation-utilisateur/{idCrypte}")
	public String initierConfirmation (Model model, @PathVariable("idCrypte") String idCrypte) throws Exception { 
		String id = idCrypte ;//cryptoSystem.decrypt(idCrypte);
		logger.info("Confirmation de l'utilisateur avec id "+id);
		Utilisateur utilisateur2 = rest.getForObject("http://biblio-zuul/biblio-security/ws/utilisateur/id/{id}",
				Utilisateur.class, Long.parseLong(id)) ;
		model.addAttribute("utilisateurExistant", utilisateur2) ;
		return "public/confirmation-utilisateur" ;
	}
	
	
	@PostMapping ("/public/abonne/parametres-connexion")
	public String confirmerAbonne (HttpServletRequest request, Model model, @RequestParam("id") Long id,
			@RequestParam("email") String email,
			@RequestParam("password") String password,
			@RequestParam("password2") String password2) throws Exception {
		try {
			
			Abonne abonne = rest.getForObject("http://biblio-zuul/biblio-operations/ws/abonne/id/{id}",
					Abonne.class, id) ;
			
			Utilisateur utilisateur = new Utilisateur() ;
			utilisateur.setEmail(email);
			utilisateur.setPassword(passwordEncoder.encode(password));
			utilisateur.setNom(abonne.getNom()+" "+abonne.getPrenom());
			utilisateur.setDateCreation(LocalDateTime.now());
			utilisateur.setDateDernMaj(LocalDateTime.now());
			
			utilisateur.setPays(abonne.getPays()) ;
			utilisateur.setRole(RoleU.Usager.getId());
			
			utilisateur.setStatut(StatutU.Valide.getId());
            utilisateur.setPhoto(abonne.getPhoto());
			
			rest.postForEntity("http://biblio-zuul/biblio-security/ws/utilisateur/ajout", utilisateur, Utilisateur.class) ;		  			
			
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);
		    authToken.setDetails(new WebAuthenticationDetails(request));
		    
		    Authentication authentication = authenticationManager.authenticate(authToken);

		    SecurityContextHolder.getContext().setAuthentication(authentication);
	
		    Utilisateur utilisateurCourant = rest.getForObject("http://biblio-zuul/biblio-security/ws/utilisateur/email/{email}",
		    		Utilisateur.class, authentication.getName()) ;
		    model.addAttribute("utilisateurCourant", utilisateurCourant) ;
		    return "redirect:/" ;
		}catch(Exception ex) {
			 model.addAttribute("idAbonne", id) ;
			 model.addAttribute("emailAbonne", email) ;
			logger.log(Level.SEVERE, ex.getMessage());
			return "public/ajout-abonne-2" ;
		}
	}
	
	
	@PostMapping ("/public/utilisateur/confirmation")
	public String confirmer (Model model, @RequestParam("id") Long id,
			@RequestParam("password") String password,
			@RequestParam("password2") String password2) throws Exception {
		try {
			
			Utilisateur utilisateur = rest.getForObject("http://biblio-zuul/biblio-security/ws/utilisateur/id/{id}",
					Utilisateur.class, id) ;
			
			utilisateur.setPassword(passwordEncoder.encode(password)) ;
			ResponseEntity<Utilisateur> response = rest.postForEntity("http://biblio-zuul/biblio-security/ws/utilisateur/confirmation", utilisateur, Utilisateur.class) ;		  
			model.addAttribute("utilisateurCourant", response.getBody()) ;
		    return "redirect:/" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			return "public/confirmation-utilisateur" ;
		}
	}
	
}
