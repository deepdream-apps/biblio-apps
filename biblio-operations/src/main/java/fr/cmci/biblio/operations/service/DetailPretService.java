package fr.cmci.biblio.operations.service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import fr.cmci.biblio.operations.data.DetailPret;
import fr.cmci.biblio.operations.data.Panier;
import fr.cmci.biblio.operations.data.Pret;
import fr.cmci.biblio.operations.repository.DetailPretRepository;
import fr.cmci.biblio.operations.repository.PanierRepository;
import fr.cmci.biblio.operations.repository.PretRepository;

@Service
@Transactional
public class DetailPretService {
	private Logger logger = Logger.getLogger(DetailPretService.class.getName()) ;
	@Autowired
	private PretRepository pretDAO ;
	@Autowired
	private DetailPretRepository detailRepository ;
	@Autowired
	private Environment env ;

	
	
	public List<Pret> rechercherStatut(String statut) throws Exception {
		try{
			logger.info("Lancement de la recherche des prets par statut");
			List<Pret> listeEmprunts = pretDAO.findByStatut(statut) ;
			return listeEmprunts ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	

	public List<DetailPret> rechercher(Pret pret) throws Exception {
		try{
			logger.info("Lancement de la recherche des prets");
			List<DetailPret> listeDetails = detailRepository.findByPret(pret) ;
			return listeDetails ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	public List<DetailPret> rechercher(DetailPret detailPret) throws Exception {
		try{
			logger.info("Lancement de la recherche des détails de prêt");
			Iterable<DetailPret> liste = detailRepository.findAll() ;
			List<DetailPret> listeDetails = new ArrayList<DetailPret>() ;
			liste.forEach(listeDetails::add);
			return listeDetails ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
}
