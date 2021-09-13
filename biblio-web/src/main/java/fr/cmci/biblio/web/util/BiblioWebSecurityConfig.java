package fr.cmci.biblio.web.util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import fr.cmci.biblio.web.service.AuthentificationService;

@Configuration
@EnableWebSecurity
public class BiblioWebSecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
    private AuthentificationService authentificationService ;

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(authentificationService) ;
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder() ;
    }
    
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider());
        //auth.inMemoryAuthentication()
        //.withUser("admin@gmail.com")
        //.password(passwordEncoder().encode("admin"))
        //.roles("Admin") ;
    }
    
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
            .csrf().disable()
          	.authorizeRequests()
          		//.antMatchers(".").permitAll()
          		//.antMatchers("/").permitAll()
          		//.antMatchers("/page-login").permitAll()
          		//.antMatchers("/page-logoff").permitAll()
          		//.antMatchers("/css/**").permitAll()
          		//.antMatchers("/img/**").permitAll()
          		//.antMatchers("/lib/**").permitAll()
          		//.antMatchers("/js/**").permitAll()
          		//.antMatchers("/scss/**").permitAll()
          		//.antMatchers("/error/**").permitAll()
          		//.antMatchers("/ws/**").permitAll()
          		//.antMatchers("/public/**").permitAll()
          		.antMatchers("/private/**").authenticated()
          	.and()
          		.sessionManagement()
          		.invalidSessionUrl("/page-login")
          	.and()
          		.formLogin()
          			.loginPage("/page-login")
          			.loginProcessingUrl("/perform_login")
          			.defaultSuccessUrl("/page-loggedin")
          			.failureUrl("/page-login") 
          	.and()
          		.logout()
          			.logoutUrl("/perform_logout")
          			.logoutSuccessUrl("/page-logoff")
          			.invalidateHttpSession(true)
          			//.deleteCookies("JSESSIONID") 
          			;

    }
    
}
