package fr.cmci.biblio.web.controlers;
import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import fr.cmci.biblio.catalog.data.Auteur;
import fr.cmci.biblio.catalog.data.Categorie;
import fr.cmci.biblio.catalog.data.Livre;
import fr.cmci.biblio.security.data.Utilisateur;

@Controller
@SessionAttributes({"utilisateurCourant"})
public class LivreCtrl implements Serializable{
	private Logger logger = Logger.getLogger(LivreCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/private/catalog/livres")
	public String index (Model model, @SessionAttribute("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		 ResponseEntity<Livre[]> response = rest.getForEntity("http://biblio-zuul/biblio-catalog/ws/livre/all", Livre[].class) ;
		 List<Livre> listeLivres = Arrays.asList(response.getBody());
		 model.addAttribute("listeLivres", listeLivres) ;
		 model.addAttribute("utilisateurCourant", utilisateurCourant) ;
		return "private/livres" ;
	}
	
	
	@GetMapping ("/private/catalog/ajout-livre")
	public String initAjout (Model model, @SessionAttribute("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		Livre livre = new Livre() ;
		livre.setCategorie1(new Categorie());
		livre.setCategorie2(new Categorie());
		livre.setAuteur(new Auteur());
		model.addAttribute("livre", livre) ;
		model.addAttribute("utilisateurCourant", utilisateurCourant) ;
		initData(model) ;
		return "private/ajout-livre" ;
	}
	
	@PostMapping ("/private/catalog/livre/ajout")
	public String ajouter (Model model, @RequestParam("titre") String titre, 
			@RequestParam("description") String description, 
			@RequestParam("anneePublication") Integer anneePublication, 
			@RequestParam("nbPages") Integer nbPages,
			@RequestParam("quantite") Integer quantite,
			@RequestParam("categorie1") String idCategorie1,
			@RequestParam("categorie2") String idCategorie2,
			@RequestParam("auteur") String idAuteur,
			@RequestParam("langue") String langue, 
			@RequestParam("file") MultipartFile file, 
			Principal principal) throws Exception {
		Livre livre = new Livre() ;
		livre.setCreateur(principal.getName());
		livre.setModificateur(principal.getName());
		
		livre.setTitre(titre);
		livre.setAnneePublication(anneePublication);
		livre.setNbPages(nbPages);
		livre.setQuantite(quantite);
		livre.setDescription(description);
		livre.setLangue(langue);
		
		Auteur auteur = rest.getForObject("http://biblio-zuul/biblio-catalog/ws/auteur/id/{id}", 
				Auteur.class, idAuteur) ;
		livre.setAuteur(auteur);
		
		Categorie categorie1 = rest.getForObject("http://biblio-zuul/biblio-catalog/ws/categorie/id/{id}", 
				Categorie.class, idCategorie1) ;
		livre.setCategorie1(categorie1);
		
		if(idCategorie2 != null && ! idCategorie2.equals("")) {
			Categorie categorie2 = rest.getForObject("http://biblio-zuul/biblio-catalog/ws/categorie/id/{id}", 
				Categorie.class, idCategorie2) ;
			livre.setCategorie2(categorie2);
		}
		
		if(file.getBytes() != null && file.getBytes().length > 0) {
			livre.setCouverture(file.getBytes()); 
		}else {
			 logger.info("Find the default user image ");
			 File defaultFile = ResourceUtils.getFile("classpath:empty-images/empty-book.jpg");
			 logger.info("Path to abonne logo "+defaultFile.getAbsolutePath());
			 byte[] allBytes = Files.readAllBytes(Paths.get(defaultFile.getAbsolutePath()));
			 livre.setCouverture(allBytes);
		 }
	
		ResponseEntity<Livre> response = rest.postForEntity("http://biblio-zuul/biblio-catalog/ws/livre/ajout", livre, Livre.class) ;		  	
		return "redirect:/private/catalog/livres" ;
	}
	
	@GetMapping ("/private/catalog/maj-livre/{id}")
	public String initMaj (Model model, @PathVariable("id")String id) throws Exception {
		Livre livre = rest.getForObject("http://biblio-zuul/biblio-catalog/ws/livre/id/{id}", Livre.class, id) ;	
		livre.setAuteur(livre.getAuteur() == null || livre.getAuteur().getId() == null || livre.getAuteur().getId().equals("") ?
				new Auteur() : livre.getAuteur());
		livre.setCategorie1(livre.getCategorie1() == null || livre.getCategorie1().getId() == null || livre.getCategorie1().getId().equals("") ?
				new Categorie() : livre.getCategorie1()) ;
		livre.setCategorie2(livre.getCategorie2() == null || livre.getCategorie2().getId() == null || livre.getCategorie2().getId().equals("") ?
				new Categorie() : livre.getCategorie2());
		model.addAttribute("livreExistant", livre) ;
		initData(model) ;
		return "private/maj-livre" ;
	}
	
	@PostMapping ("/private/catalog/livre/maj")
	public String modifier (Model model, @RequestParam("id") String id, 
			@RequestParam("titre") String titre, 
			@RequestParam("description") String description, 
			@RequestParam("anneePublication") Integer anneePublication, 
			@RequestParam("nbPages") Integer nbPages,
			@RequestParam("quantite") Integer quantite, 
			@RequestParam("categorie1") String idCategorie1,
			@RequestParam("categorie2") String idCategorie2,
			@RequestParam("auteur") String idAuteur,
			@RequestParam("langue") String langue, 
			@RequestParam("file") MultipartFile file, 
			Principal principal) throws Exception {
		Livre livre = rest.getForObject("http://biblio-zuul/biblio-catalog/ws/livre/id/{id}", Livre.class, id) ;
		livre.setCreateur(principal.getName());
		livre.setModificateur(principal.getName());
		
		livre.setTitre(titre);
		livre.setAnneePublication(anneePublication);
		livre.setNbPages(nbPages);
		livre.setDescription(description);
		livre.setQuantite(quantite);
		livre.setLangue(langue);
		
		Auteur auteur = rest.getForObject("http://biblio-zuul/biblio-catalog/ws/auteur/id/{id}", 
				Auteur.class, idAuteur) ;
		livre.setAuteur(auteur);
		
		Categorie categorie1 = rest.getForObject("http://biblio-zuul/biblio-catalog/ws/categorie/id/{id}", 
				Categorie.class, idCategorie1) ;
		livre.setCategorie1(categorie1);
		
		if(idCategorie2 != null && ! idCategorie2.equals("")) {
			Categorie categorie2 = rest.getForObject("http://biblio-zuul/biblio-catalog/ws/categorie/id/{id}", 
				Categorie.class, idCategorie2) ;
			livre.setCategorie2(categorie2);
		}
		
		if(file != null && file.getBytes() != null && file.getBytes().length > 0) {
			livre.setCouverture(file.getBytes()); 
		}else {
			 logger.info("Find the default user image ");
			 File defaultFile = ResourceUtils.getFile("classpath:empty-images/empty-book.jpg");
			 logger.info("Path to abonne logo "+defaultFile.getAbsolutePath());
			 byte[] allBytes = Files.readAllBytes(Paths.get(defaultFile.getAbsolutePath()));
			 livre.setCouverture(allBytes);
		 }
	
		ResponseEntity<Livre> response = rest.postForEntity("http://biblio-zuul/biblio-catalog/ws/livre/maj", livre, Livre.class) ;		  	
		return "redirect:/private/catalog/livres" ;
	}
	
	private void initData(Model model) {
		ResponseEntity<Auteur[]> response = rest.getForEntity("http://biblio-zuul/biblio-catalog/ws/auteur/all", Auteur[].class) ;
		List<Auteur> listeAuteurs = Arrays.asList(response.getBody());
		model.addAttribute("auteurs", listeAuteurs) ;
		
		ResponseEntity<Categorie[]> response2 = rest.getForEntity("http://biblio-zuul/biblio-catalog/ws/categorie/all", Categorie[].class) ;
		List<Categorie> listeCategories = Arrays.asList(response2.getBody());
		model.addAttribute("categories", listeCategories) ;
	}
}
