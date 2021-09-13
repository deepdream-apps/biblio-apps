package fr.cmci.biblio.operations.webservice;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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

import fr.cmci.biblio.catalog.data.Auteur;
import fr.cmci.biblio.catalog.data.Categorie;
import fr.cmci.biblio.catalog.data.Livre;
import fr.cmci.biblio.operations.data.Abonne;
import fr.cmci.biblio.operations.data.DetailPret;
import fr.cmci.biblio.operations.data.Panier;
import fr.cmci.biblio.operations.data.Pret;
import fr.cmci.biblio.operations.service.DetailPretService;
import fr.cmci.biblio.operations.service.PretService;
import fr.cmci.biblio.operations.wrapper.AbonneCount;
import fr.cmci.biblio.operations.wrapper.AuteurCount;
import fr.cmci.biblio.operations.wrapper.CategorieCount;
import fr.cmci.biblio.operations.wrapper.LivreCount;

@RestController
@RequestMapping (path = "/ws/detailpret")
public class DetailPretWS {
	private Logger logger = Logger.getLogger(DetailPretWS.class.getName()) ;
	@Autowired
	private PretService pretService ;
	@Autowired
	private DetailPretService detailPretService ;
	
	
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
	
	@GetMapping("/pret/{idPret}")
	public List<DetailPret> getAllDetails (@PathVariable("idPret") Long idPret) {
		try {
			Pret pret = pretService.rechercher(idPret) ;
			List<DetailPret> listeDetails = detailPretService.rechercher(pret) ;
			return listeDetails ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<DetailPret>() ;
		}
	}
	
	@GetMapping("/top-livres/{nombre}")
	public List<LivreCount> getTopnLivres (@PathVariable("nombre") Integer nombre) {
		try {
			DetailPret detail = new DetailPret() ;
			List<DetailPret> prets = detailPretService.rechercher(detail) ;
			
			List<Livre> livres = prets.stream().map(DetailPret::getLivre).collect(Collectors.toList()) ;
			
			Map<Livre, Long> map = livres.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting())) ;
			
			List<LivreCount> livreCounts = new ArrayList<LivreCount>() ;
			
			map.forEach((livre,count) -> livreCounts.add(new LivreCount(livre, count)));
			
			List<LivreCount> topLivres = livreCounts.stream().sorted((lc1, lc2) ->{
				return lc2.getNbDemandes().compareTo(lc1.getNbDemandes()) ;
			}).collect(Collectors.toList()).subList(0, Math.min(nombre, livreCounts.size())) ;
			
			return topLivres ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<LivreCount>() ;
		}
	}
	
	
	@GetMapping("/top-categories/{nombre}")
	public List<CategorieCount> getTopnCategories (@PathVariable("nombre") Integer nombre) {
		try {
			DetailPret detail = new DetailPret() ;
			List<DetailPret> prets = detailPretService.rechercher(detail) ;
			
			List<Livre> livres = prets.stream().map(DetailPret::getLivre).collect(Collectors.toList()) ;
			
			Map<Categorie, Long> map = livres.stream().collect(Collectors.groupingBy(Livre::getCategorie1, Collectors.counting())) ;
			
			List<CategorieCount> categoriesCounts = new ArrayList<CategorieCount>() ;
			
			map.forEach((categorie,count) -> categoriesCounts.add(new CategorieCount(categorie, count)));
			
			List<CategorieCount> topCategories = categoriesCounts.stream().sorted((cat1, cat2) ->{
				return cat2.getNbDemandes().compareTo(cat1.getNbDemandes()) ;
			}).collect(Collectors.toList()).subList(0, Math.min(categoriesCounts.size(), nombre)) ;
			
			return topCategories ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<CategorieCount>() ;
		}
	}
	
	
	@GetMapping("/top-auteurs/{nombre}")
	public List<AuteurCount> getTopnAuteurs (@PathVariable("nombre") Integer nombre) {
		try {
			DetailPret detail = new DetailPret() ;
			List<DetailPret> prets = detailPretService.rechercher(detail) ;
			
			List<Livre> livres = prets.stream().map(DetailPret::getLivre).collect(Collectors.toList()) ;
			
			Map<Auteur, Long> map = livres.stream().collect(Collectors.groupingBy(Livre::getAuteur, Collectors.counting())) ;
			
			List<AuteurCount> auteurCounts = new ArrayList<AuteurCount>() ;
			
			map.forEach((auteur,count) -> auteurCounts.add(new AuteurCount(auteur, count)));
			
			List<AuteurCount> topAuteurs = auteurCounts.stream().sorted((au1, au2) ->{
				return au2.getNbDemandes().compareTo(au1.getNbDemandes()) ;
			}).collect(Collectors.toList()).subList(0, Math.min(auteurCounts.size(), nombre)) ;
			
			return topAuteurs ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<AuteurCount>() ;
		}
	}
	
	@GetMapping("/top-abonnes/{nombre}")
	public List<AbonneCount> getTopnAbonnes (@PathVariable("nombre") Integer nombre) {
		try {
			Pret pret = new Pret() ;
			List<Pret> prets = pretService.rechercher(pret) ;
			
			List<Abonne> abonnes = prets.stream().map(Pret::getAbonne).collect(Collectors.toList()) ;
			
			Map<Abonne, Long> map = prets.stream().collect(Collectors.groupingBy(Pret::getAbonne, Collectors.counting())) ;
			
			List<AbonneCount> abonneCounts = new ArrayList<AbonneCount>() ;
			
			map.forEach((auteur,count) -> abonneCounts.add(new AbonneCount(auteur, count)));
			
			List<AbonneCount> topAbonnes = abonneCounts.stream().sorted((au1, au2) ->{
				return au2.getNbDemandes().compareTo(au1.getNbDemandes()) ;
			}).collect(Collectors.toList()).subList(0, Math.min(abonneCounts.size(), nombre)) ;
			
			return topAbonnes ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<AbonneCount>() ;
		}
	}
	
}
