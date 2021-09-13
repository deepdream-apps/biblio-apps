package fr.cmci.biblio.operations.webservice;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.cmci.biblio.operations.data.Abonne;
import fr.cmci.biblio.operations.data.Panier;
import fr.cmci.biblio.operations.service.PanierService;

@RestController
@RequestMapping (path = "/ws/panier")
public class PanierWS {
	private Logger logger = Logger.getLogger(PanierWS.class.getName()) ;
	@Autowired
	private PanierService panierService ;
	
	
	@PostMapping("/maj")
	@ResponseStatus(code =  HttpStatus.OK)
	public Panier modifier (@RequestBody Panier panier) {
		try {
			Panier panierMaj = panierService.modifier(panier) ;
			return panierMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	

	@DeleteMapping("/suppr/{id}")
	public int suppr (@PathVariable ("id") Long id) {
		try {
			Panier panier = panierService.rechercher(id) ;
			panierService.supprimer(panier) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/abonne/{id}")
	public Panier getByAbonne (@PathVariable("id") Long id) {
		try {
			Abonne abonne = new Abonne() ;
			abonne.setId(id);
			Panier panier = panierService.rechercher(abonne) ;
			return panier ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/id/{id}")
	public Panier getById (@PathVariable("id") Long id) {
		try {
			Panier panier = panierService.rechercher(id) ;
			return panier ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	
}
