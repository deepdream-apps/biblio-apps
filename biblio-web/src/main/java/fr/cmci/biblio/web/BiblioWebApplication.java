package fr.cmci.biblio.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

@SpringBootApplication
public class BiblioWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(BiblioWebApplication.class, args);
	}
	
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate() ;
		return restTemplate ;
	}
	
	@Bean
    public Java8TimeDialect java8TimeDialect() {
        return new Java8TimeDialect();
    }

}
