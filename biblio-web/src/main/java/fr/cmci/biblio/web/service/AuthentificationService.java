package fr.cmci.biblio.web.service;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import fr.cmci.biblio.security.data.Utilisateur;

@Service
public class AuthentificationService implements UserDetailsService{	
	private Logger logger = Logger.getLogger(AuthentificationService.class.getName()) ;
	@LoadBalanced 
	@Autowired
	private RestTemplate rest ;
	@Autowired
    private LoginAttemptService loginAttemptService;
	@Autowired
    private HttpServletRequest request ;
 

    
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    	try {
    		String ip = getClientIP();
    		if (loginAttemptService.isBlocked(ip)) {
    			throw new RuntimeException("blocked") ;
    		}
    		Utilisateur utilisateur = rest.getForObject("http://biblio-zuul/biblio-security/ws/utilisateur/email/{email}", Utilisateur.class, email) ;
    		
    		UserDetails details = User.withUsername(utilisateur.getEmail()).password(utilisateur.getPassword()).authorities(utilisateur.getLibelleRole()).build();
    		return details ;
    	}catch(Exception ex) {
    		logger.log(Level.SEVERE, ex.getMessage(), ex); ;
    		throw ex ;
    	}
    }
    
    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null){
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
