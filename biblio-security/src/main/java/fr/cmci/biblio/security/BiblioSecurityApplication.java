package fr.cmci.biblio.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
@EntityScan("fr.cmci.biblio.security.data") 
@EnableJpaRepositories(basePackages = "fr.cmci.biblio.security.repository")
@SpringBootApplication
@EnableEurekaClient
public class BiblioSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(BiblioSecurityApplication.class, args);
	}

}
