package fr.cmci.biblio.oauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableResourceServer
public class BiblioOauth2Application {

	public static void main(String[] args) {
		SpringApplication.run(BiblioOauth2Application.class, args);
	}

}
