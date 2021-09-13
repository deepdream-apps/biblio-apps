package fr.cmci.biblio.web.controlers;
import java.io.Serializable;
import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;

import fr.cmci.biblio.catalog.data.Auteur;
import fr.cmci.biblio.catalog.data.Livre;
import fr.cmci.biblio.operations.data.Abonne;
import fr.cmci.biblio.operations.data.Panier;
import fr.cmci.biblio.operations.wrapper.AbonneCount;
import fr.cmci.biblio.operations.wrapper.AuteurCount;
import fr.cmci.biblio.operations.wrapper.CategorieCount;
import fr.cmci.biblio.operations.wrapper.LivreCount;
import fr.cmci.biblio.security.data.Session;
import fr.cmci.biblio.security.data.Utilisateur;
import fr.cmci.biblio.security.enums.RoleU;

@Controller
@SessionAttributes({"utilisateurCourant", "listeAuteurs", "auteurCourant", "pageCourante", "nbPages", "listeLivres",
	"panier", "topLivres", "topAuteurs", "topAbonnes", "top5Livres", "topCategories"})
public class BiblioCtrl implements Serializable{
	private Logger logger = Logger.getLogger(BiblioCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	private Integer NB_LIVRES_PAGE = 30 ;

	
	 @GetMapping ("/")
	 public String dashboardDefault (Model model) {
		 ResponseEntity<Auteur[]> response = rest.getForEntity("http://biblio-zuul/biblio-catalog/ws/auteur/all", Auteur[].class) ;
		 List<Auteur> listeAuteurs = Arrays.asList(response.getBody());
		 model.addAttribute("listeAuteurs", listeAuteurs) ;
		 
		 Auteur auteurCourant = listeAuteurs.size() == 0 ? null : listeAuteurs.get(0) ;
		 
		 if(auteurCourant != null) {
			 ResponseEntity<Livre[]> response2 = rest.getForEntity("http://biblio-zuul/biblio-catalog/ws/livre/auteur/id/{id}", 
				 Livre[].class, auteurCourant.getId()) ;
			 List<Livre> listeLivres = Arrays.asList(response2.getBody());
			 model.addAttribute("listeLivres", listeLivres) ;
			 model.addAttribute("nbLivres", listeLivres.size()) ;
			 model.addAttribute("auteurCourant", auteurCourant) ;
			 model.addAttribute("pageCourante", 1) ;
			 model.addAttribute("nbPages", listeLivres.size()/NB_LIVRES_PAGE + 
					 (Math.floorMod(listeLivres.size(), NB_LIVRES_PAGE) == 0 ? 0:1)) ;
			 model.addAttribute("nbLivres", listeLivres.size()) ;
			 model.addAttribute("listeLivres_Page", 
					 listeLivres.size() == 0 ? new ArrayList<Livre>():listeLivres.subList(0, Math.min(NB_LIVRES_PAGE, listeLivres.size()))) ;
		 } else {
			 model.addAttribute("auteurCourant", null) ;
			 model.addAttribute("pageCourante", 1) ;
			 model.addAttribute("nbPages", 1) ;
			 model.addAttribute("nbLivres", 0) ;
			 model.addAttribute("listeLivres", new ArrayList<Livre>()) ;
			 model.addAttribute("listeLivres_Page", new ArrayList<Livre>()) ;
		 }
		 
		 model.addAttribute("listeAuteurs", listeAuteurs) ;
		 
	     return "public/dashboard" ;
	 }
	 
	 @GetMapping ("/auteur/id/{id}")
	 public String dashboardAuteur (Model model, @PathVariable("id")String id) {
		 Auteur auteurCourant = rest.getForObject("http://biblio-zuul/biblio-catalog/ws/auteur/id/{id}", Auteur.class, id) ;
		 
		 if(auteurCourant != null) {
			 ResponseEntity<Livre[]> response2 = rest.getForEntity("http://biblio-zuul/biblio-catalog/ws/livre/auteur/id/{id}", 
				 Livre[].class, auteurCourant.getId()) ;
			 List<Livre> listeLivres = Arrays.asList(response2.getBody());
			 model.addAttribute("listeLivres", listeLivres) ;
			 model.addAttribute("nbLivres", listeLivres.size()) ;
			 model.addAttribute("auteurCourant", auteurCourant) ;
			 model.addAttribute("pageCourante", 1) ;
			 model.addAttribute("nbPages", listeLivres.size()/NB_LIVRES_PAGE + 
					 (Math.floorMod(listeLivres.size(), NB_LIVRES_PAGE) == 0 ? 0:1)) ;
			 model.addAttribute("listeLivres", listeLivres) ;
			 List<Livre> listeLivres_Page = listeLivres.subList(0, Math.min(listeLivres.size() , NB_LIVRES_PAGE)) ;
			 model.addAttribute("listeLivres_Page", listeLivres_Page) ;
		 } else {
			 model.addAttribute("auteurCourant", null) ;
			 model.addAttribute("pageCourante", 1) ;
			 model.addAttribute("nbPages", 1) ;
			 model.addAttribute("nbLivres", 0) ;
			 model.addAttribute("listeLivres", new ArrayList<Livre>()) ;
			 model.addAttribute("listeLivres_Page", new ArrayList<Livre>()) ;
		 }
		 
		 ResponseEntity<Auteur[]> response2 = rest.getForEntity("http://biblio-zuul/biblio-catalog/ws/auteur/all", Auteur[].class) ;
		 List<Auteur> listeAuteurs = Arrays.asList(response2.getBody());
		 model.addAttribute("listeAuteurs", listeAuteurs) ;
		 
	     return "public/dashboard" ;
	 }
	 
	 
	 @GetMapping ("/page/{numPage}")
	 public String dashboardAuteur_Page (Model model, @PathVariable("numPage")Integer numPage, 
			 @SessionAttribute("listeLivres") List<Livre> listeLivres,
			 @SessionAttribute("nbPages") Integer nbPages,
			 @SessionAttribute("pageCourante") Integer pageCourante,
			 @SessionAttribute("auteurCourant") Auteur auteurCourant) {
		
		if(numPage > nbPages )
			numPage = nbPages ;
		if(numPage <= 0 )
			numPage = 1 ;
		model.addAttribute("listeLivres", listeLivres) ;
		model.addAttribute("nbLivres", listeLivres.size()) ;
		model.addAttribute("auteurCourant", auteurCourant) ;
		model.addAttribute("pageCourante", numPage) ;
		model.addAttribute("nbPages", nbPages) ;
		model.addAttribute("nbLivres", listeLivres.size()) ;
		List<Livre> listeLivres_Page = listeLivres.subList((numPage-1)*NB_LIVRES_PAGE, 
				Math.min(listeLivres.size() , numPage*NB_LIVRES_PAGE)) ;
		
		model.addAttribute("listeLivres_Page", listeLivres_Page) ;
		
		ResponseEntity<Auteur[]> response2 = rest.getForEntity("http://biblio-zuul/biblio-catalog/ws/auteur/all", Auteur[].class) ;
		List<Auteur> listeAuteurs = Arrays.asList(response2.getBody());
		model.addAttribute("listeAuteurs", listeAuteurs) ;
		 
	    return "public/dashboard" ;
	 }
	 
	 
	 @GetMapping ("/public/page-livre/{id}")
	 public String pageLivre (Model model, @PathVariable("id") String id) {
		 
		ResponseEntity<Auteur[]> response2 = rest.getForEntity("http://biblio-zuul/biblio-catalog/ws/auteur/all", Auteur[].class) ;
		List<Auteur> listeAuteurs = Arrays.asList(response2.getBody());
		model.addAttribute("listeAuteurs", listeAuteurs) ;
		
		Livre livre = rest.getForObject("http://biblio-zuul/biblio-catalog/ws/livre/id/{id}", Livre.class, id) ;
		model.addAttribute("livreCourant", livre) ;
			
	    return "public/page-livre" ;
	 }
	 
	 @GetMapping ("/page-login")
	 public String pageLogin1 (Model model) {
	       return "public/page-login" ;
	 }
	 
	 @GetMapping ("/page-loggedin")
	 public String connecter (Model model, Principal principal) {
		 logger.log(Level.INFO, "Authentification de l'utilisateur "+principal.getName());
		 String email = principal.getName() ;
		 Utilisateur utilisateur = rest.getForObject("http://biblio-zuul/biblio-security/ws/utilisateur/email/{email}", 
				Utilisateur.class, email) ;
		 model.addAttribute("utilisateurCourant", utilisateur) ;
		 
		 if(RoleU.Usager.getId() == utilisateur.getRole()) {
			 Abonne abonne = rest.getForObject("http://biblio-zuul/biblio-operations/ws/abonne/email/{email}", 
					  Abonne.class, principal.getName()) ;
			  
			  Panier panier = rest.getForObject("http://biblio-zuul/biblio-operations/ws/panier/abonne/{id}", 
					  Panier.class, abonne.getId()) ;
			  
			  model.addAttribute("panier", panier) ;
		     return "redirect:/" ; 
		 }
		 
		 model.addAttribute("panier", new Panier()) ;
		 
		 ResponseEntity<LivreCount[]> response0 = rest.getForEntity("http://biblio-zuul/biblio-operations/ws/detailpret/top-livres/{nombre}", 
				 LivreCount[].class, 10) ;
		 List<LivreCount> topLivres = Arrays.asList(response0.getBody());
		 model.addAttribute("topLivres", topLivres) ;
		 
		 model.addAttribute("top5Livres", topLivres.subList(0, Math.min(5, topLivres.size()))) ;
		 
		 ResponseEntity<AuteurCount[]> response1 = rest.getForEntity("http://biblio-zuul/biblio-operations/ws/detailpret/top-auteurs/{nombre}", 
				 AuteurCount[].class, 5) ;
		 List<AuteurCount> topAuteurs = Arrays.asList(response1.getBody());
		 model.addAttribute("topAuteurs", topAuteurs) ;
		 
		 ResponseEntity<CategorieCount[]> response3 = rest.getForEntity("http://biblio-zuul/biblio-operations/ws/detailpret/top-categories/{nombre}", 
				 CategorieCount[].class, 5) ;
		 List<CategorieCount> topCategories = Arrays.asList(response3.getBody());
		 model.addAttribute("topCategories", topCategories) ;
		 
		 ResponseEntity<AbonneCount[]> response2 = rest.getForEntity("http://biblio-zuul/biblio-operations/ws/detailpret/top-abonnes/{nombre}", 
				 AbonneCount[].class, 5) ;
		 List<AbonneCount> topAbonnes = Arrays.asList(response2.getBody());
		 model.addAttribute("topAbonnes", topAbonnes) ;
		 
	     return "redirect:/private/dashboard" ;
	 }
	 
	 @GetMapping ("/page-logoff")
	 public String deconnecter (Model model) {
		 model.addAttribute("utilisateurCourant", null) ;
	     return "/" ;
	 }
	 
	 
	 @GetMapping ("/change-password")
	 public String changePwd (Model model) {
	       return "change-password" ;
	 }
	 
	 @GetMapping ("/private/dashboard")
	 public String dashboard0 (Model model) {
	       return "private/dashboard" ;
	 }
	 
	 @GetMapping ("/public/dashboard")
	 public String dashboard1 (Model model) {
	       return "public/dashboard" ;
	 }
	 
	 @GetMapping ("/error")
	 public String pageErreur (Model model) {
	       return "extra-404" ;
	 }
	 
	 @GetMapping ("/dashboard")
	 public String dashboard (Model model, HttpServletRequest request) throws Exception{
		 try {
			 Authentication authentication = SecurityContextHolder.getContext().getAuthentication() ;
			 String login = authentication.getName() ;
			 Utilisateur utilisateur = rest.getForObject("http://academia-zuul/academia-security/ws/utilisateur/login/{login}", Utilisateur.class, login) ;
			 
			 if (utilisateur == null) {
				 throw new SecurityException ("Echec ! Login ou mot de passe incorrect") ;
			 }
			 if (Duration.between(LocalDateTime.now(), utilisateur.getDateExpiration()).isNegative()) {
				 throw new SecurityException ("Echec ! Mot de passe expiré") ;
			 }
			 if (Duration.between(LocalDateTime.now(), utilisateur.getDateExpirationMdp()).isNegative() ) {
				 throw new SecurityException ("Echec ! Votre compte a expiré") ;
			 }
			 
			 model.addAttribute("utilisateurCourant", utilisateur) ;			

			 Session session = new Session() ;
			 session.setUtilisateur(utilisateur) ;
			 session.setDateDebut(LocalDateTime.now()) ;
			 session.setAdresseIP(request.getRemoteAddr());
			 rest.postForEntity("http://biblio-zuul/biblio-security/ws/session/ajout", session, Session.class);

		     return "dashboard" ;
		 }catch(SecurityException sex) {
			 model.addAttribute("messageEchec", sex.getMessage()) ;
			 logger.log(Level.SEVERE, sex.getMessage(), sex) ;
			 return "login";
		 }catch(Exception ex) {
			 model.addAttribute("messageEchec", "Echec de l'opération") ;
			 logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			 return "login";
		 }
	 }
	 
	 @GetMapping ("/info-profil")
	 public String infoProfil (Model model, HttpServletRequest request)  throws Exception{
		/* Authentication authentication = SecurityContextHolder.getContext().getAuthentication() ;
		 String login = authentication.getName() ;
		 Utilisateur utilisateur = rest.getForObject("http://biblio-zuul/dreamscool-securite/utilisateur/{login}", Utilisateur.class, login) ;
		 model.addAttribute("utilisateurCourant", utilisateur) ;
		 Map<String,Long> urlVariables = new HashMap<>();
		 urlVariables.put("idUtilisateur", utilisateur.getId()) ;
		 List<Session> listeDernieresConnexions = rest.getForObject("http://dreamscool-zuul/dreamscool-securite/session/search/{idUtilisateur}", 
				 List.class, urlVariables) ;
		 model.addAttribute("listeDernieresConnexions", listeDernieresConnexions.subList(listeDernieresConnexions.size() - 5 >=0 ? listeDernieresConnexions.size()-5 : 0, 
				 listeDernieresConnexions.size())) ;*/
		 
	     return "info-profil" ;
	 }
	 
	 @GetMapping ("/maj-mot-de-passe")
	 public String initMajMdp (Model model, HttpServletRequest request)  throws Exception{
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication() ;
		 String login = authentication.getName() ;
		 Utilisateur utilisateur = rest.getForObject("http://biblio-zuul/biblio-security/utilisateur/{login}", Utilisateur.class, login) ;
		 model.addAttribute("currentUser", utilisateur) ;
	     return "maj-mot-de-passe" ;
	 }
	 
	 @PostMapping ("/mot-de-passe/maj")
	 public String majMdp (Model model, Utilisateur utilisateurCourant) throws Exception{
		// String mdp1 = utilisateurCourant.getMdp1() ;
		// String mdp2 = utilisateurCourant.getMdp2() ;
		// String mdp3 = utilisateurCourant.getMdp3() ;
		 
		 /*  if(! new BCryptPasswordEncoder().encode(mdp1).equals(utilisateurCourant.getMotDePasse())) {
			 model.addAttribute("messageEchec", "Echec ! Ancien mot de passe incorrect") ;
			 return "maj-mot-de-passe" ;
		 }
		 
		if(! mdp2.equals(mdp3)) {
			 model.addAttribute("messageEchec", "Confirmation de mot de passe incorrecte") ;
			 return "maj-mot-de-passe" ;
		 }
		 
		 utilisateurCourant.setMotDePasse(new BCryptPasswordEncoder().encode(mdp2)) ;*/
		 
		 rest.put("http://dreamscool-zuul/dreamscool-securite/utilisateur/{id}", utilisateurCourant, utilisateurCourant.getId()) ;
		 
		 model.addAttribute("messageSuccess", "Mot de passe mis jour") ;
	     return "maj-mot-de-passe" ;
	 }
	 
}
