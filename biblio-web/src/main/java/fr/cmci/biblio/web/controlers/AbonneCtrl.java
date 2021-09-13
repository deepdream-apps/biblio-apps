package fr.cmci.biblio.web.controlers;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
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
import fr.cmci.biblio.operations.data.Abonne;
import fr.cmci.biblio.web.util.CryptoSystem;

@Controller
@SessionAttributes({"abonneCourant", "listeAuteurs"})
public class AbonneCtrl implements Serializable{
	private Logger logger = Logger.getLogger(AbonneCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	@Autowired
	private CryptoSystem cryptoSystem ;
	
	@GetMapping ("/private/operations/abonnes")
	public String index (Model model) throws Exception {
		 ResponseEntity<Abonne[]> response = rest.getForEntity("http://biblio-zuul/biblio-operations/ws/abonne/all", Abonne[].class) ;
		 List<Abonne> listeAbonnes = Arrays.asList(response.getBody());
		 model.addAttribute("listeAbonnes", listeAbonnes) ;
		return "private/abonnes" ;
	}
	
	@GetMapping ("/public/ajout-abonne-1")
	public String initAjout0 (Model model, @SessionAttribute("listeAuteurs") List<Auteur> listeAuteurs) throws Exception {
		model.addAttribute("listeAuteurs", listeAuteurs) ;
		model.addAttribute("abonneCourant", new Abonne()) ;
		remplirModele(model, new Abonne()) ;
		return "public/ajout-abonne-1" ;
	}
	
	
	@PostMapping ("/public/abonne/ajout1")
	public String ajouter (Model model, @RequestParam("nom") String nom, 
			@RequestParam("prenom") String prenom, 
			@RequestParam("genre") String genre, 
			@RequestParam("email") String email, 
			@RequestParam("anneeConv") Integer anneeConv, 
			@RequestParam("communaute") String communaute, 
			@RequestParam("telephone") String telephone, 
			@RequestParam("pays") String pays, 
			@RequestParam("ville") String ville, 
			@RequestParam("codePostal") String codePostal,
			@RequestParam("adresse") String adresse,
			@RequestParam("file") MultipartFile file) throws Exception {
			
		Abonne abonne = new Abonne() ;
		 try {
			 abonne.setNom(nom);
			 abonne.setPrenom(prenom);
			 abonne.setGenre(genre);
			 abonne.setEmail(email);
			 abonne.setTelephone(telephone);
			 abonne.setPays(pays);
			 abonne.setVille(ville);
			 abonne.setCodePostal(codePostal);
			 abonne.setAdresse(adresse);
			 abonne.setAnneeConv(anneeConv);
			 abonne.setCommunaute(communaute);
			 
			 if(file.getBytes() != null && file.getBytes().length > 0) {
				 abonne.setPhoto(file.getBytes());
			 }else {
				 logger.info("Find the default user image ");
				 File defaultFile = ResourceUtils.getFile("classpath:empty-images/unknow-person.gif");
				 logger.info("Path to abonne logo "+defaultFile.getAbsolutePath());
				 byte[] allBytes = Files.readAllBytes(Paths.get(defaultFile.getAbsolutePath()));
		         abonne.setPhoto(allBytes);
			 }
			 
			 ResponseEntity<Abonne> response = rest.postForEntity("http://biblio-zuul/biblio-operations/ws/abonne/ajout", 
				  abonne, Abonne.class) ;		  
			 model.addAttribute("email", abonne.getEmail()) ;
			 return "public/confirmation-email-abonne" ;
		 }catch(Exception ex) {
			 logger.log(Level.SEVERE, ex.getMessage()) ;
			 remplirModele(model, abonne) ;
			 return "public/ajout-abonne-1" ;
		 }
	}
	
	
	@GetMapping ("/public/confirmation-email-abonne/{id}")
	public String confirmerEmail (Model model, @PathVariable("id") Long id) throws Exception {
		Abonne abonne = rest.getForObject("http://biblio-zuul/biblio-operations/ws/abonne/id/{id}", Abonne.class, id) ;
		model.addAttribute("idAbonne", abonne.getId()) ;
		model.addAttribute("emailAbonne", abonne.getEmail()) ;
		return "public/ajout-abonne-2" ;
	}
	
	
	@GetMapping ("/private/admin/maj-abonne/{id}")
	public String initMaj (Model model, @PathVariable("id")String id) throws Exception {
		Abonne abonne = rest.getForObject("http://biblio-zuul/biblio-catalog/ws/abonne/id/{id}", Abonne.class, id) ;		  
		model.addAttribute("abonneExistant", abonne) ;
		return "private/maj-abonne" ;
	}
	
	
	@PostMapping ("/private/admin/abonne/maj")
	public String modifier (Model model, @RequestParam("id") String id, 
			@RequestParam("nom") String nom, 
			@RequestParam("anneeNaissance") Integer anneeNaissance, 
			@RequestParam("anneeDeces") Integer anneeDeces, 
			@RequestParam("biographie1") String biographie1, 
			@RequestParam("biographie2") String biographie2, 
			@RequestParam("pays") String pays, 
			@RequestParam("file") MultipartFile file, 
			Principal principal) throws Exception {
		
		  Abonne abonne = rest.getForObject("http://biblio-zuul/biblio-catalog/ws/abonne/id/{id}", Abonne.class, id) ;
		  abonne.setCreateur(principal.getName());
		  abonne.setModificateur(principal.getName());
	
		  ResponseEntity<Abonne> response = rest.postForEntity("http://biblio-zuul/biblio-catalog/ws/abonne/maj", abonne, Abonne.class) ;		  
		 //model.addAttribute("listeAbonnes", listeAbonnes) ;
		  return "redirect:/private/admin/abonnes" ;
	}
	
	private void remplirModele(Model model, Abonne abonne) {
		model.addAttribute("id", abonne.getId()) ;
		model.addAttribute("nom", abonne.getNom()) ;
		model.addAttribute("prenom", abonne.getPrenom()) ;
		model.addAttribute("genre", abonne.getGenre()) ;
		model.addAttribute("anneeConv", abonne.getAnneeConv()) ;
		model.addAttribute("communaute", abonne.getCommunaute()) ;
		model.addAttribute("pays", abonne.getPays()) ;
		model.addAttribute("ville", abonne.getVille()) ;
		model.addAttribute("codePostal", abonne.getCodePostal()) ;
		model.addAttribute("adresse", abonne.getAdresse()) ;
		model.addAttribute("email", abonne.getEmail()) ;
		model.addAttribute("nbEmprunts", abonne.getNbEmprunts()) ;
		model.addAttribute("nbLivres", abonne.getNbLivres()) ;
	}
	
}
