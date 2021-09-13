package fr.cmci.biblio.web.api;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import fr.cmci.biblio.security.data.Utilisateur;

@RestController
@RequestMapping("/ws/utilisateur/web")
public class UtilisateurAPI {
	private Logger logger = Logger.getLogger(UtilisateurAPI.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	
	@ResponseBody
	@GetMapping (path = "/login/{login}")
	public Boolean existe (@PathVariable("login") String login) throws Exception{
		logger.log(Level.INFO, "Est-ce que "+login+" existe ?") ;
		Utilisateur utilisateur = rest.getForObject("http://academia-zuul/academia-security/ws/utilisateur/login/{login}", Utilisateur.class, login) ;
		return utilisateur != null ;
	}
}
