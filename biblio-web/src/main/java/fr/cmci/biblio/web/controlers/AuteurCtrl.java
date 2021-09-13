package fr.cmci.biblio.web.controlers;
import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
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

import fr.cmci.biblio.catalog.data.Auteur;
import fr.cmci.biblio.security.data.Utilisateur;

@Controller
@SessionAttributes({"auteurCourant", "utilisateurCourant"})
public class AuteurCtrl implements Serializable{
	private Logger logger = Logger.getLogger(AuteurCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/private/catalog/auteurs")
	public String index (Model model, @SessionAttribute("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		 ResponseEntity<Auteur[]> response = rest.getForEntity("http://biblio-zuul/biblio-catalog/ws/auteur/all", Auteur[].class) ;
		 List<Auteur> listeAuteurs = Arrays.asList(response.getBody());
		 model.addAttribute("listeAuteurs", listeAuteurs) ;
		 model.addAttribute("utilisateurCourant", utilisateurCourant) ;
		 return "private/auteurs" ;
	}
	
	@GetMapping ("/private/catalog/ajout-auteur")
	public String initAjout (Model model) throws Exception {
		model.addAttribute("auteur", new Auteur()) ;
		return "private/ajout-auteur" ;
	}
	
	@PostMapping ("/private/catalog/auteur/ajout")
	public String ajouter (Model model, @RequestParam("nom") String nom, 
			@RequestParam("anneeNaissance") Integer anneeNaissance, 
			@RequestParam("anneeDeces") Integer anneeDeces, 
			@RequestParam("biographie1") String biographie1, 
			@RequestParam("biographie2") String biographie2, 
			@RequestParam("pays") String pays, 
			@RequestParam("file") MultipartFile file, 
			Principal principal) throws Exception {		
		Auteur auteur = new Auteur() ;
		try {  
		  logger.log(Level.INFO, "Ajout d'un auteur "+nom) ;
		  auteur.setCreateur(principal.getName());
		  auteur.setModificateur(principal.getName());
		  
		  auteur.setNom(nom);
		  auteur.setAnneeNaissance(anneeNaissance);
		  auteur.setAnneeDeces(anneeDeces);
		  auteur.setBiographie1(biographie1);
		  auteur.setBiographie2(biographie2);
		  auteur.setPays(pays);
		  auteur.setPhoto(file.getBytes());
		  
		  rest.postForEntity("http://biblio-zuul/biblio-catalog/ws/auteur/ajout", auteur, Auteur.class) ;		  
		 //model.addAttribute("listeAuteurs", listeAuteurs) ;
		  logger.log(Level.INFO, "Auteur ajouté "+auteur) ;
		  return "redirect:/private/catalog/auteurs" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			return "/private/catalog/ajout-auteur";
		}
	}
	
	@GetMapping ("/private/catalog/maj-auteur/{id}")
	public String initMaj (Model model, @PathVariable("id")String id) throws Exception {
		Auteur auteur = rest.getForObject("http://biblio-zuul/biblio-catalog/ws/auteur/id/{id}", Auteur.class, id) ;		  
		model.addAttribute("auteurExistant", auteur) ;
		return "private/maj-auteur" ;
	}
	
	@PostMapping ("/private/catalog/auteur/maj")
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
		
		  Auteur auteur = rest.getForObject("http://biblio-zuul/biblio-catalog/ws/auteur/id/{id}", Auteur.class, id) ;
		  auteur.setCreateur(principal.getName());
		  auteur.setModificateur(principal.getName());
		  
		  auteur.setNom(nom);
		  auteur.setAnneeNaissance(anneeNaissance);
		  auteur.setAnneeDeces(anneeDeces);
		  auteur.setBiographie1(biographie1);
		  auteur.setBiographie2(biographie2);
		  auteur.setPays(pays);

		  auteur.setOrdre(ordre);
		  if(file != null && file.getBytes() != null)
			  auteur.setPhoto(file.getBytes());
		  
		  ResponseEntity<Auteur> response = rest.postForEntity("http://biblio-zuul/biblio-catalog/ws/auteur/maj", auteur, Auteur.class) ;		  
		 //model.addAttribute("listeAuteurs", listeAuteurs) ;
		  return "redirect:/private/catalog/auteurs" ;
	}
}
