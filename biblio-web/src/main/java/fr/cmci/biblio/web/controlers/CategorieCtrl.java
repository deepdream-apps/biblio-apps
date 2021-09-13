package fr.cmci.biblio.web.controlers;
import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;

import fr.cmci.biblio.catalog.data.Categorie;
import fr.cmci.biblio.security.data.Utilisateur;

@Controller
@SessionAttributes({"utilisateurCourant"})
public class CategorieCtrl implements Serializable{
	private Logger logger = Logger.getLogger(CategorieCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/private/catalog/categories")
	public String index (Model model, @SessionAttribute("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		 ResponseEntity<Categorie[]> response = rest.getForEntity("http://biblio-zuul/biblio-catalog/ws/categorie/all", Categorie[].class) ;
		 List<Categorie> listeCategories = Arrays.asList(response.getBody());
		 model.addAttribute("listeCategories", listeCategories) ;
		 model.addAttribute("utilisateurCourant", utilisateurCourant) ;
		 return "private/categories" ;
	}
	
	@GetMapping ("/private/catalog/ajout-categorie")
	public String initAjout (Model model) throws Exception {
		model.addAttribute("categorie", new Categorie()) ;
		return "private/ajout-categorie" ;
	}
	
	@PostMapping ("/private/catalog/categorie/ajout")
	public String ajouter (Model model, Categorie categorie, Principal principal) throws Exception {
		  categorie.setCreateur(principal.getName());
		  categorie.setModificateur(principal.getName());
		  ResponseEntity<Categorie> response = rest.postForEntity("http://biblio-zuul/biblio-catalog/ws/categorie/ajout", categorie, Categorie.class) ;		  
		 //model.addAttribute("listeCategories", listeCategories) ;
		return "redirect:/private/catalog/categories" ;
	}
	
	@GetMapping ("/private/catalog/maj-categorie/{id}")
	public String initMaj (Model model, @PathVariable("id")String id) throws Exception {
		Categorie categorie = rest.getForObject("http://biblio-zuul/biblio-catalog/ws/categorie/id/{id}", Categorie.class, id) ;		  
		model.addAttribute("categorieExistante", categorie) ;
		return "private/maj-categorie" ;
	}
	
	@PostMapping ("/private/catalog/categorie/maj")
	public String maj (Model model, Categorie categorieExistante, Principal principal) throws Exception {
		  Categorie categorie = rest.getForObject("http://biblio-zuul/biblio-catalog/ws/categorie/id/{id}", 
				  Categorie.class, categorieExistante.getId()) ;
		  categorie.setCreateur(principal.getName());
		  categorie.setModificateur(principal.getName());
		  
		  categorie.setLibelle(categorieExistante.getLibelle());
		  categorie.setDescription(categorieExistante.getDescription());
		  rest.postForEntity("http://biblio-zuul/biblio-catalog/ws/categorie/maj", categorie, Categorie.class) ;		  
		 //model.addAttribute("listeCategories", listeCategories) ;
		  return "redirect:/private/catalog/categories" ;
	}
}
