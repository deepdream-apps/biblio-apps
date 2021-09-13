package fr.cmci.biblio.web.rest;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import fr.cmci.biblio.catalog.data.Auteur;
import fr.cmci.biblio.catalog.data.Livre;
import fr.cmci.biblio.operations.data.Abonne;
import fr.cmci.biblio.security.data.Utilisateur;

@RestController
public class ImageREST implements Serializable{
	 private static final String EXTENSION = ".jpg";
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	
	@RequestMapping(path = "/livre/couverture/{id}", method = RequestMethod.GET)
    public ResponseEntity<Resource> couverture(@PathVariable("id") String id) throws IOException {
		Livre livre = rest.getForObject("http://biblio-zuul/biblio-catalog/ws/livre/id/{id}", Livre.class, id) ;	

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=img"+System.currentTimeMillis()+".jpg");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        ByteArrayResource resource = new ByteArrayResource(livre.getCouverture());

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(livre.getCouverture().length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    } 
	
	
	@RequestMapping(path = "/auteur/photo/{id}", method = RequestMethod.GET)
    public ResponseEntity<Resource> photo(@PathVariable("id") String id) throws IOException {
		Auteur auteur = rest.getForObject("http://biblio-zuul/biblio-catalog/ws/auteur/id/{id}", Auteur.class, id) ;	

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=img"+System.currentTimeMillis()+".jpg");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        ByteArrayResource resource = new ByteArrayResource(auteur.getPhoto());

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(auteur.getPhoto().length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    } 
	
	
	@RequestMapping(path = "/utilisateur/photo/{id}", method = RequestMethod.GET)
    public ResponseEntity<Resource> photoAbonne(@PathVariable("id") String id) throws IOException {
		Utilisateur utilisateur = rest.getForObject("http://biblio-zuul/biblio-security/ws/utilisateur/id/{id}", Utilisateur.class, id) ;	

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=img"+System.currentTimeMillis()+".jpg");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        ByteArrayResource resource = new ByteArrayResource(utilisateur.getPhoto());

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(utilisateur.getPhoto().length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    } 

}
