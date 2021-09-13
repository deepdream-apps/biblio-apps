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
import fr.cmci.biblio.operations.service.AbonneService;

@RestController
@RequestMapping (path = "/ws/abonne")
public class AbonneWS {
	private Logger logger = Logger.getLogger(AbonneWS.class.getName()) ;
	@Autowired
	private AbonneService abonneService ;
	
	@PostMapping("/ajout")
	@ResponseStatus(code =  HttpStatus.CREATED)
	public Abonne ajout (@RequestBody Abonne abonne) {
		try {
			Abonne abonneCree = abonneService.creer(abonne) ;
			return abonneCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PostMapping("/maj")
	@ResponseStatus(code =  HttpStatus.OK)
	public Abonne maj (@RequestBody Abonne abonne) {
		try {
			Abonne abonneMaj = abonneService.modifier(abonne) ;
			return abonneMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	

	@DeleteMapping("/suppr/{id}")
	public int suppr (@PathVariable ("id") Long id) {
		try {
			Abonne abonne = abonneService.rechercher(id) ;
			abonneService.supprimer(abonne) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/id/{id}")
	public Abonne getById (@PathVariable("id") Long id) {
		try {
			Abonne abonne = abonneService.rechercher(id) ;
			return abonne ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	
	@GetMapping("/email/{email}")
	public Abonne getByEmail (@PathVariable("email") String email) {
		try {
			Abonne abonne = abonneService.rechercher(email) ;
			return abonne ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/all")
	public List<Abonne> getAll () {
		try {
			Abonne abonne = new Abonne() ;
			List<Abonne> listeAbonnes = abonneService.rechercher(abonne) ;
			return listeAbonnes ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Abonne>() ;
		}
	}
	
}
