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
import fr.cmci.biblio.catalog.data.Livre;
import fr.cmci.biblio.catalog.service.AuteurService;
import fr.cmci.biblio.catalog.service.LivreService;

@RestController
@RequestMapping (path = "/ws/livre")
public class LivreWS {
	private Logger logger = Logger.getLogger(LivreWS.class.getName()) ;
	@Autowired
	private LivreService livreService ;
	@Autowired
	private AuteurService auteurService ;
	
	@PostMapping("/ajout")
	@ResponseStatus(code =  HttpStatus.CREATED)
	public Livre ajout (@RequestBody Livre livre) {
		try {
			Livre livreCree = livreService.creer(livre) ;
			return livreCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PostMapping("/maj")
	@ResponseStatus(code =  HttpStatus.OK)
	public Livre maj (@RequestBody Livre livre) {
		try {
			Livre livreMaj = livreService.modifier(livre) ;
			return livreMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr/{id}")
	public int suppr (@PathVariable ("id") Long id) {
		try {
			Livre livre = livreService.rechercher(id) ;
			livreService.supprimer(livre) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/id/{id}")
	public Livre getById (@PathVariable("id") Long id) {
		try {
			Livre livre = livreService.rechercher(id) ;
			return livre ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/auteur/id/{id}")
	public List<Livre> getLivres_Auteur (@PathVariable("id") Long id) {
		try {
			Auteur auteur = auteurService.rechercher(id) ;
			List<Livre> listeLivres = livreService.rechercher(auteur) ;
			return listeLivres ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Livre>() ;
		}
	}
	
	@GetMapping("/all")
	public List<Livre> getAll () {
		try {
			Livre livre = new Livre() ;
			List<Livre> listeLivres = livreService.rechercher(livre) ;
			return listeLivres ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Livre>() ;
		}
	}
	
}
