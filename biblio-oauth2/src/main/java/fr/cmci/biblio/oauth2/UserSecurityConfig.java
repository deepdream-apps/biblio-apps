package fr.cmci.biblio.oauth2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
@Configuration
public class UserSecurityConfig extends WebSecurityConfigurerAdapter{
	 	@Bean
	    public AuthenticationManager getAuthenticationManager() throws Exception {
	        return super.authenticationManagerBean();
	    }
}
