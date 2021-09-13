package fr.cmci.biblio.operations;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
@EntityScan(basePackages = {"fr.cmci.biblio.operations.data", "fr.cmci.biblio.catalog.data"})
@EnableJpaRepositories(basePackages = "fr.cmci.biblio.operations.repository")
@SpringBootApplication
@EnableEurekaClient
public class BiblioOperationsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BiblioOperationsApplication.class, args);
	}

}
