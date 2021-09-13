package fr.cmci.biblio.security.data;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode (callSuper = false)
public class Utilisateur extends EntiteGenerique{
	@Id
	@Column (name = "id")
	private  Long id ;
	
	@NotNull
	@Column (name = "email", unique = true)
	private  String email ;
	
	@NotNull
	@Column (name = "nom")
	private String nom ;
	
	@Column (name = "prenom")
	private String prenom ;
	
	@Column (name = "password")
	private  String password ;
	
	@Column (name = "telephone")
	private  String telephone ;
	
	@Column (name = "pays")
	private  String pays ;
	
	@Column (name = "date_expiration")
	@DateTimeFormat (pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime dateExpiration ;
	
	@Column (name = "date_exp_mdp")
	@DateTimeFormat (pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime dateExpirationMdp;
	
	@Column (name = "date_susp")
	@DateTimeFormat (pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime dateSusp ;
	
	@Column (name = "statut")
	private  Integer statut ;
	
	@Column (name = "libelle_statut")
	private  String libelleStatut ;
	
	@Column (name = "role")
	private  Integer role ;
	
	@Column (name = "libelle_role")
	private  String libelleRole ;
	
	@Lob
	@Column (name = "photo")
	private byte[] photo ;
}
