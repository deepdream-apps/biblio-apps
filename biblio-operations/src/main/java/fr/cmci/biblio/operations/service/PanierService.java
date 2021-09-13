package fr.cmci.biblio.operations.service;
import java.io.BufferedInputStream;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.cmci.biblio.catalog.data.Livre;
import fr.cmci.biblio.operations.data.Abonne;
import fr.cmci.biblio.operations.data.Panier;
import fr.cmci.biblio.operations.repository.PanierRepository;


@Service
@Transactional
public class PanierService {
	private Logger logger = Logger.getLogger(PanierService.class.getName()) ;
	@Autowired
	private PanierRepository panierDAO ;
	@Autowired
	private Environment env ;

	
	public void supprimer(Panier panier) throws Exception {
		try{
			logger.info("Lancement de la suppression d'un panier");
			panierDAO.delete(panier) ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public Panier rechercher(Long id) throws Exception {
		try{
			logger.info("Lancement de la recherche d'un panier");
			Panier panier = panierDAO.findById(id).orElseThrow(Exception::new) ;
			return panier ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	

	public Panier rechercher(Abonne abonne) throws Exception {
		try{
			logger.info("Lancement de la recherche du panier de "+abonne);
			Panier panier = panierDAO.findByAbonne(abonne) ;
			
			if(panier == null) {
				panier = new Panier() ;
				panier.setAbonne(abonne);
				panier.setNbLivres(0);
				panier.setListeLivres(new ArrayList<Livre>());
				panierDAO.save(panier) ;
			}
			return panier ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}


	public Panier modifier(Panier panier) throws Exception {
		try{
			logger.info("Lancement de la modification d'un panier");
			panier.setDateDernMaj(LocalDateTime.now());
			panierDAO.save(panier) ;
			return panier ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
}
