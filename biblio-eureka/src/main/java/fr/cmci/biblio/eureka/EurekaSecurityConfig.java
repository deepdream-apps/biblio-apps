package fr.cmci.biblio.eureka;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@EnableWebSecurity
public class EurekaSecurityConfig extends WebSecurityConfigurerAdapter {
	@Value("${app.biblio.eureka.username}")
	private String userName ;
	
	@Value("${app.biblio.eureka.password}")
	private String password ;
	
	@Value("${app.biblio.eureka.role}")
	private String roleName ;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth)throws Exception {
		auth
			.inMemoryAuthentication()
				.withUser(userName).password(passwordEncoder().encode(password)).authorities("ROLE_"+roleName) ;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder() ;
	}

   @Override
   protected void configure(HttpSecurity http) throws Exception{
	   http 
	   .csrf()
	   		.disable()
       .authorizeRequests()
       		.antMatchers("/").hasRole("ADMIN")
       		.anyRequest().hasRole("ADMIN")
       .and()
       		.httpBasic() ;
   }
   
}
