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
import fr.cmci.biblio.catalog.data.Categorie;
import fr.cmci.biblio.catalog.service.CategorieService;

@RestController
@RequestMapping (path = "/ws/categorie")
public class CategorieWS {
	private Logger logger = Logger.getLogger(CategorieWS.class.getName()) ;
	@Autowired
	private CategorieService categorieService ;
	
	@PostMapping("/ajout")
	@ResponseStatus(code =  HttpStatus.CREATED)
	public Categorie ajout (@RequestBody Categorie categorie) {
		try {
			Categorie categorieCree = categorieService.creer(categorie) ;
			return categorieCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PostMapping("/maj")
	@ResponseStatus(code =  HttpStatus.OK)
	public Categorie maj (@RequestBody Categorie categorie) {
		try {
			Categorie categorieMaj = categorieService.modifier(categorie) ;
			return categorieMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	

	@DeleteMapping("/suppr/{id}")
	public int suppr (@PathVariable ("id") Long id) {
		try {
			Categorie categorie = categorieService.rechercher(id) ;
			categorieService.supprimer(categorie) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/id/{id}")
	public Categorie getById (@PathVariable("id") Long id) {
		try {
			Categorie categorie = categorieService.rechercher(id) ;
			return categorie ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/all")
	public List<Categorie> getAll () {
		try {
			Categorie categorie = new Categorie() ;
			List<Categorie> listeCategories = categorieService.rechercher(categorie) ;
			return listeCategories ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Categorie>() ;
		}
	}
	
}
