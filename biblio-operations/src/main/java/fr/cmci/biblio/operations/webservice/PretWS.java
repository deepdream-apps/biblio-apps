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

import fr.cmci.biblio.operations.data.Panier;
import fr.cmci.biblio.operations.data.Pret;
import fr.cmci.biblio.operations.service.PretService;

@RestController
@RequestMapping (path = "/ws/pret")
public class PretWS {
	private Logger logger = Logger.getLogger(PretWS.class.getName()) ;
	@Autowired
	private PretService pretService ;
	
	@PostMapping("/ajout")
	@ResponseStatus(code =  HttpStatus.CREATED)
	public Pret ajout (@RequestBody Panier panier) {
		try {
			Pret pretCree = pretService.creer(panier) ;
			return pretCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PostMapping("/accord")
	public Pret accord (@RequestBody Pret pret) {
		try {
			Pret pretAccorde = pretService.accorder(pret) ;
			return pretAccorde ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PostMapping("/refus")
	public Pret refus (@RequestBody Pret pret) {
		try {
			Pret pretRefuse = pretService.refuser(pret) ;
			return pretRefuse ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PostMapping("/validation-envoi")
	public Pret validerEnvoi (@RequestBody Pret pret) {
		try {
			Pret pretRefuse = pretService.validerEnvoi(pret) ;
			return pretRefuse ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PostMapping("/maj")
	@ResponseStatus(code =  HttpStatus.OK)
	public Pret maj (@RequestBody Pret pret) {
		try {
			Pret pretMaj = pretService.modifier(pret) ;
			return pretMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	

	@DeleteMapping("/suppr/{id}")
	public int suppr (@PathVariable ("id") Long id) {
		try {
			Pret pret = pretService.rechercher(id) ;
			pretService.supprimer(pret) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/id/{id}")
	public Pret getById (@PathVariable("id") Long id) {
		try {
			Pret pret = pretService.rechercher(id) ;
			return pret ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	
	@GetMapping("/statut/{statut}")
	public List<Pret> getEmpruntsStatut(@PathVariable ("statut") String statut) {
		try {
			Pret pret = new Pret() ;
			List<Pret> listeEmprunts = pretService.rechercherStatut(statut) ;
			return listeEmprunts ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Pret>() ;
		}
	}
	
	
	
	@GetMapping("/all")
	public List<Pret> getAll () {
		try {
			Pret pret = new Pret() ;
			List<Pret> listeEmprunts = pretService.rechercher(pret) ;
			return listeEmprunts ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Pret>() ;
		}
	}
	
}
