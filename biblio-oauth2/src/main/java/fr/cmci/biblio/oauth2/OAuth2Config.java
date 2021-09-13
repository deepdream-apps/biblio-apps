package fr.cmci.biblio.oauth2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
@Configuration
@EnableAuthorizationServer
public class OAuth2Config extends AuthorizationServerConfigurerAdapter{
	@Autowired
	private AuthenticationManager authenticationManager ;
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpointsConfigurer) throws Exception{
		endpointsConfigurer.authenticationManager(authenticationManager) ;
	} 

	@Override
	public void configure(ClientDetailsServiceConfigurer clientDetailsServiceConfigurer) throws Exception{
		clientDetailsServiceConfigurer
			.inMemory()
				.withClient("academiaweb")
				.secret("academiawebsecret")
				.authorizedGrantTypes("authorization_code", "refresh_token", "implicit", "password", "client_id", "secret")
				.scopes("academiarestapi")
			.and()
				.withClient("academiamobile")
				.secret("academiamobilesecret")
				.authorizedGrantTypes("authorization_code", "refresh_token", "implicit", "password", "client_id", "secret")
				.scopes("academiarestapi") 
			.and()
				.withClient("academiamobile")
				.secret("academiamobilesecret")
				.authorizedGrantTypes("authorization_code", "refresh_token", "implicit", "password", "client_id", "secret")
				.scopes("academiarestapi") ;
	}
	
	
}
