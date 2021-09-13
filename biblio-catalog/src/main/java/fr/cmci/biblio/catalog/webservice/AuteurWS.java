package fr.cmci.biblio.catalog.webservice;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import fr.cmci.biblio.catalog.data.Auteur;
import fr.cmci.biblio.catalog.service.AuteurService;

@RestController
@RequestMapping (path = "/ws/auteur")
public class AuteurWS {
	private Logger logger = Logger.getLogger(AuteurWS.class.getName()) ;
	@Autowired
	private AuteurService auteurService ;
	
	@PostMapping("/ajout")
	@ResponseStatus(code =  HttpStatus.CREATED)
	public Auteur ajout (@RequestBody Auteur auteur) {
		try {
			Auteur auteurCree = auteurService.creer(auteur) ;
			return auteurCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PostMapping("/maj")
	@ResponseStatus(code =  HttpStatus.OK)
	public Auteur maj (@RequestBody Auteur auteur) {
		try {
			Auteur auteurMaj = auteurService.modifier(auteur) ;
			return auteurMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/maj-mot-de-passe")
	@ResponseStatus(code =  HttpStatus.OK)
	public Auteur majMdp (@RequestBody Auteur auteur) {
		try {
			Auteur auteurMaj = auteurService.modifier(auteur) ;
			return auteurMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr/{id}")
	public int suppr (@PathVariable ("id") Long id) {
		try {
			Auteur auteur = auteurService.rechercher(id) ;
			auteurService.supprimer(auteur) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/id/{id}")
	public Auteur getById (@PathVariable("id") Long id) {
		try {
			Auteur auteur = auteurService.rechercher(id) ;
			return auteur ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/all")
	public List<Auteur> getAll () {
		try {
			Auteur auteur = new Auteur() ;
			List<Auteur> listeAuteurs = auteurService.rechercher(auteur) ;
			return listeAuteurs ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Auteur>() ;
		}
	}
	
}
