package fr.cmci.biblio.catalog.service;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.cmci.biblio.catalog.data.Auteur;
import fr.cmci.biblio.catalog.repository.AuteurRepository;

@Service
@Transactional
public class AuteurService {
	private Logger logger = Logger.getLogger(AuteurService.class.getName()) ;
	@Autowired
	private AuteurRepository auteurDAO ;

	@Autowired
	private Environment env ;

	public Auteur creer(Auteur auteur) throws Exception {
		try{
			logger.info("Lancement de la creation d'un auteur") ;	
			auteur.setDateCreation(LocalDateTime.now());
			auteur.setDateDernMaj(LocalDateTime.now());
			auteur.setOrdre(auteurDAO.count());
			auteurDAO.save(auteur) ;
			return auteur ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex;
		}
	}

	
	public void supprimer(Auteur auteur) throws Exception {
		try{
			logger.info("Lancement de la suppression d'un auteur");
			auteurDAO.delete(auteur) ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public Auteur rechercher(Long id) throws Exception {
		try{
			logger.info("Lancement de la recherche d'un auteur");
			Auteur auteur = auteurDAO.findById(id).orElseThrow(Exception::new) ;
			return auteur ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	

	public List<Auteur> rechercher(Auteur auteur) throws Exception {
		try{
			logger.info("Lancement de la recherche des auteurs");
			Iterable<Auteur> auteurs = auteurDAO.findAll() ;
			
			List<Auteur> listeAuteurs = new ArrayList<Auteur>() ;
			auteurs.forEach(listeAuteurs::add) ;
			List<Auteur> listeAuteurs2 =	listeAuteurs.stream().sorted((a1, a2)->{
				return a1.getOrdre().compareTo(a2.getOrdre()) ;
			}).collect(Collectors.toList()) ;
			return listeAuteurs2 ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}


	public Auteur modifier(Auteur auteur) throws Exception {
		try{
			logger.info("Lancement de la modification d'un auteur");
			auteur.setDateDernMaj(LocalDateTime.now());
			if(auteur.getOrdre() != null)
				auteur.setOrdre(auteurDAO.count());
			auteurDAO.save(auteur) ;
			return auteur ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
}
