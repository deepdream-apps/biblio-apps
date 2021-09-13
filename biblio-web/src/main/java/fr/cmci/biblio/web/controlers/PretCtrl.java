package fr.cmci.biblio.web.controlers;
import java.io.Serializable;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import fr.cmci.biblio.catalog.data.Livre;
import fr.cmci.biblio.operations.data.Abonne;
import fr.cmci.biblio.operations.data.DetailPret;
import fr.cmci.biblio.operations.data.Pret;
import fr.cmci.biblio.operations.enums.StatutP;
import fr.cmci.biblio.operations.data.Panier;
import fr.cmci.biblio.security.data.Utilisateur;
import fr.cmci.biblio.security.enums.RoleU;
import fr.cmci.biblio.security.enums.StatutU;

@Controller
@SessionAttributes({"panier", "utilisateurCourant"})
public class PretCtrl implements Serializable{
	private Logger logger = Logger.getLogger(PretCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	
	@Autowired
	private Environment env ;
	
	@GetMapping ("/private/operations/prets")
	public String index (Model model, @SessionAttribute("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		 ResponseEntity<Pret[]> response = rest.getForEntity("http://biblio-zuul/biblio-operations/ws/pret/all", Pret[].class) ;
		 List<Pret> listeEmprunts = Arrays.asList(response.getBody());
		 model.addAttribute("listePrets", listeEmprunts) ;
		 model.addAttribute("utilisateurCourant", utilisateurCourant) ;
		 return "private/prets" ;
	}
	
	
	@GetMapping ("/private/operations/demandes-enattente")
	public String indexDemandesEnAttente (Model model, @SessionAttribute("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		 ResponseEntity<Pret[]> response = rest.getForEntity("http://biblio-zuul/biblio-operations/ws/pret/statut/{statut}", 
				 Pret[].class, StatutP.En_Attente.toString()) ;
		 List<Pret> listePrets = Arrays.asList(response.getBody());
		 List<Pret> listePretsOrdonnes = listePrets.stream().sorted((p1,p2)->{
			 return p2.getDateCreation().compareTo(p1.getDateCreation()) ;
		 }).collect(Collectors.toList()) ;
		 model.addAttribute("listePrets", listePretsOrdonnes) ;
		 model.addAttribute("utilisateurCourant", utilisateurCourant) ;
		 return "private/demandes-enattente" ;
	}
	
	@GetMapping ("/private/operations/demandes-acceptees")
	public String indexDemandesAcceptees(Model model, @SessionAttribute("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		 ResponseEntity<Pret[]> response = rest.getForEntity("http://biblio-zuul/biblio-operations/ws/pret/statut/{statut}", 
				 Pret[].class, StatutP.Accepte.toString()) ;
		 List<Pret> listePrets = Arrays.asList(response.getBody());
		 List<Pret> listePretsOrdonnes = listePrets.stream().sorted((p1,p2)->{
			 return p2.getDateCreation().compareTo(p1.getDateCreation()) ;
		 }).collect(Collectors.toList()) ;
		 model.addAttribute("listePrets", listePretsOrdonnes) ;
		 model.addAttribute("utilisateurCourant", utilisateurCourant) ;
		 return "private/demandes-acceptees" ;
	}
	
	@GetMapping ("/private/operations/demandes-refusees")
	public String indexDemandesRefusees(Model model, @SessionAttribute("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		 ResponseEntity<Pret[]> response = rest.getForEntity("http://biblio-zuul/biblio-operations/ws/pret/statut/{statut}", 
				 Pret[].class, StatutP.Refuse.toString()) ;
		 List<Pret> listePrets = Arrays.asList(response.getBody());
		 List<Pret> listePretsOrdonnes = listePrets.stream().sorted((p1,p2)->{
			 return p2.getDateCreation().compareTo(p1.getDateCreation()) ;
		 }).collect(Collectors.toList()) ;
		 model.addAttribute("listePrets", listePretsOrdonnes) ;
		 model.addAttribute("utilisateurCourant", utilisateurCourant) ;
		 return "private/demandes-refusees" ;
	}
	
	@GetMapping ("/private/operations/demandes-acheminees")
	public String indexDemandesAcheminees(Model model, @SessionAttribute("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		 ResponseEntity<Pret[]> response = rest.getForEntity("http://biblio-zuul/biblio-operations/ws/pret/statut/{statut}", 
				 Pret[].class, StatutP.Achemine.toString()) ;
		 List<Pret> listePrets = Arrays.asList(response.getBody());
		 List<Pret> listePretsOrdonnes = listePrets.stream().sorted((p1,p2)->{
			 return p2.getDateCreation().compareTo(p1.getDateCreation()) ;
		 }).collect(Collectors.toList()) ;
		 model.addAttribute("listePrets", listePretsOrdonnes) ;
		 model.addAttribute("utilisateurCourant", utilisateurCourant) ;
		 return "private/demandes-acheminees" ;
	}
	
	@GetMapping ("/private/operations/panier/ajout-livre/{idLivre}")
	public String ajouterPanier (Model model, @PathVariable("idLivre") Long idLivre, Authentication authentication) throws Exception {		
		try {  
		  logger.log(Level.INFO, "Ajout du livre au panier "+idLivre) ;
		  
		  if(authentication == null ) {
			  return "redirect:/page-login" ;
		  }
		  
		  Utilisateur utilisateur = rest.getForObject("http://biblio-zuul/biblio-security/ws/utilisateur/email/{email}", 
				  Utilisateur.class, authentication.getName()) ;
		  
				  
		  if(RoleU.Usager.getId() != utilisateur.getRole()) {
			  return "redirect:/page-login" ;
		  }
		  
		  Abonne abonne = rest.getForObject("http://biblio-zuul/biblio-operations/ws/abonne/email/{email}", Abonne.class, authentication.getName()) ;
		  
		  Panier panier = rest.getForObject("http://biblio-zuul/biblio-operations/ws/panier/abonne/{id}", Panier.class, abonne.getId()) ;
		 
		  Livre livre = rest.getForObject("http://biblio-zuul/biblio-catalog/ws/livre/id/{id}", Livre.class, idLivre) ;
		  
		  model.addAttribute("panier", panier) ;

		  if(! panier.getListeLivres().contains(livre))
			  panier.getListeLivres().add(livre) ;
		  		  logger.log(Level.INFO, "Livre ajouté "+livre) ;
		  		  
		  rest.postForObject("http://biblio-zuul/biblio-operations/ws/panier/maj", panier, Panier.class) ;
		  		  
		  return "redirect:/public/page-livre/"+livre.getId() ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			return null ;
		}
	}

	
	@GetMapping ("/public/pret/affichage-panier")
	public String afficherPanier (Model model) throws Exception {
		return "public/panier" ;
	}
	
	
	@GetMapping ("/private/pret/suppression-livre/{idLivre}")
	public String supprimerLivre (Model model, @SessionAttribute("panier") Panier panier,
			@PathVariable("idLivre") Long idLivre, Principal principal) throws Exception {		
		try {  
		  logger.log(Level.INFO, "Mise à jour du panier "+idLivre) ;
		  logger.log(Level.INFO, "Nombre de livres avant suppression "+panier.getListeLivres().size()) ;
		  panier.getListeLivres().removeIf(l -> l.getId().equals(idLivre) );
		  logger.log(Level.INFO, "Nombre de livres après suppression "+panier.getListeLivres().size()) ;
		  rest.postForObject("http://biblio-zuul/biblio-operations/ws/panier/maj", panier, Panier.class) ;
		  model.addAttribute("panier", panier) ;
		  logger.log(Level.INFO, "Panier mis à jour ") ;
		  return  panier.getListeLivres().size() == 0 ?"redirect:/" : "public/panier" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			return "redirect:/public/pret/affichage-panier";
		}
	}
	
	@GetMapping ("/private/pret/validation")
	public String ajouter (Model model, @SessionAttribute("panier") Panier panier, Principal principal) throws Exception {		
		try {  
		  logger.log(Level.INFO, "Ajout d'un pret "+panier) ;

		  logger.log(Level.INFO, "Ajout d'un pret de l'abonné "+panier) ;
		 
		  rest.postForEntity("http://biblio-zuul/biblio-operations/ws/pret/ajout", panier, Panier.class) ;	
		  panier.getListeLivres().removeIf(livre -> {return true;});
		  model.addAttribute("panier", panier) ;
		  logger.log(Level.INFO, "Pret ajouté ") ;
		  return "redirect:/" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			return "redirect:/public/pret/affichage-panier";
		}
	}
	
	
	@GetMapping ("/private/operations/pret/accord/{id}")
	public String accepter (Model model, @PathVariable("id") Long id,
			Principal principal) throws Exception {		
		try {  
		  Pret pret = rest.getForObject("http://biblio-zuul/biblio-operations/ws/pret/id/{id}", Pret.class, id) ;	
		  logger.log(Level.INFO, "Accord d'un pret "+pret) ;
		  rest.postForObject("http://biblio-zuul/biblio-operations/ws/pret/accord", pret, Pret.class) ;
		  logger.log(Level.INFO, "Pret accordé ") ;
		  return "redirect:/private/operations/demandes-acceptees" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			return "redirect:/public/pret/affichage-panier";
		}
	}
	
	@GetMapping ("/private/operations/pret/validation-envoi/{id}")
	public String validerEnvoi (Model model, @PathVariable("id") Long id,
			Principal principal) throws Exception {		
		try {  
		  Pret pret = rest.getForObject("http://biblio-zuul/biblio-operations/ws/pret/id/{id}", Pret.class, id) ;	
		  logger.log(Level.INFO, "Accord d'un pret "+pret) ;
		  rest.postForObject("http://biblio-zuul/biblio-operations/ws/pret/validation-envoi", pret, Pret.class) ;
		  logger.log(Level.INFO, "Pret acheminé ") ;
		  return "redirect:/private/operations/demandes-acheminees" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			return "redirect:/public/pret/affichage-panier";
		}
	}
	
	
	@GetMapping ("/private/operations/pret/refus/{id}")
	public String refuser (Model model, @PathVariable("id") Long id,
			Principal principal) throws Exception {		
		try {  
		  Pret pret = rest.getForObject("http://biblio-zuul/biblio-operations/ws/pret/id/{id}", Pret.class, id) ;
		  logger.log(Level.INFO, "Refus d'un pret "+pret) ;
		  rest.postForObject("http://biblio-zuul/biblio-operations/ws/pret/refus", pret, Pret.class) ;
		  logger.log(Level.INFO, "Pret refusé ") ;
		  return "redirect:/private/operations/demandes-refusees" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			return "redirect:/public/pret/affichage-panier";
		}
	}
	
	@GetMapping ("/private/operations/maj-pret/{id}")
	public String initMaj (Model model, @PathVariable("id")String id) throws Exception {
		Pret pret = rest.getForObject("http://biblio-zuul/biblio-operations/ws/pret/id/{id}", Pret.class, id) ;		  
		model.addAttribute("pretExistant", pret) ;
		return "private/maj-pret" ;
	}
	
	@GetMapping("/private/operations/details-demandepret/{idPret}")
	public String detailsPret(Model model, @PathVariable("idPret") Long idPret) {
		Pret pret = rest.getForObject("http://biblio-zuul/biblio-operations/ws/pret/id/{id}", Pret.class, idPret) ;	
		ResponseEntity<DetailPret[]> response = rest.getForEntity("http://biblio-zuul/biblio-operations/ws/detailpret/pret/{idPret}", 
				DetailPret[].class, pret.getId()) ;
		logger.log(Level.INFO, "Liste des livres "+Arrays.asList(response.getBody()));
		model.addAttribute("pretExistant", pret) ;
		model.addAttribute("listeDetails", Arrays.asList(response.getBody())) ;
		if(StatutP.En_Attente.toString().equals(pret.getStatut())) {
			return "private/detail-demandepret-enattente" ;
		} else if(StatutP.Accepte.toString().equals(pret.getStatut())) {
			return "private/detail-demandepret-acceptee" ;
		} else if(StatutP.Refuse.toString().equals(pret.getStatut())) {
			return "private/detail-demandepret-refusee" ;
		} else if(StatutP.Achemine.toString().equals(pret.getStatut())) {
			return "private/detail-demandepret-acheminee" ;
		}
		return null ;
		
	}
	
	@PostMapping ("/private/operations/pret/maj")
	public String modifier (Model model, @RequestParam("id") String id, 

			@RequestParam("nom") String nom, 
			@RequestParam("anneeNaissance") Integer anneeNaissance, 
			@RequestParam("anneeDeces") Integer anneeDeces, 
			@RequestParam("biographie1") String biographie1, 
			@RequestParam("biographie2") String biographie2, 
			@RequestParam("pays") String pays, 
			@RequestParam("ordre") Long ordre, 
			@RequestParam("file") MultipartFile file, 
			Principal principal) throws Exception {
		
		  Pret pret = rest.getForObject("http://biblio-zuul/biblio-operations/ws/pret/id/{id}", Pret.class, id) ;
		  pret.setCreateur(principal.getName());
		  pret.setModificateur(principal.getName());
	
		  ResponseEntity<Pret> response = rest.postForEntity("http://biblio-zuul/biblio-operations/ws/pret/maj", pret, Pret.class) ;		  
		 //model.addAttribute("listeEmprunts", listeEmprunts) ;
		  return "redirect:/private/operations/prets" ;
	}
}
