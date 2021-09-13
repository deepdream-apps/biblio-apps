package fr.cmci.biblio.operations.data;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode (callSuper = false)
public class Abonne extends EntiteGenerique{
	@Id
	@GeneratedValue
	@Column (name = "id")
	private Long id ;
	
	@Column (name = "nom")
	private String nom ;
	
	@Column (name = "prenom")
	private String prenom ;
	
	@Column (name = "genre")
	private String genre ;
	
	@Column (name = "annee_conv")
	private Integer anneeConv ;
	
	@Column (name = "communaute")
	private String communaute ;
	
	@Column (name = "pays")
	private String pays ;
	
	@Column (name = "ville")
	private String ville ;
	
	@Column (name = "code_postal")
	private String codePostal ;
	
	@Column (name = "adresse")
	private String adresse ;
	
	@Column (name = "email")
	private String email ;
	
	@Column (name = "telephone")
	private String telephone ;
	
	@Column (name = "nb_emprunts")
	private Integer nbEmprunts ;
	
	@Column (name = "nb_emprunts_nr")
	private Integer nbEmpruntsNr ;
	
	@Column (name = "nb_retours_hd")
	private Integer nbRetoursHd ;
	
	@Column (name = "nb_retours_dd")
	private Integer nbRetoursDd ;
	
	@Column (name = "nb_livres")
	private Integer nbLivres ;
	
	@Column (name = "nb_livres_non_retournes")
	private Integer nbLivresNonRetournes ;
	
	@Column(name = "dernier_emprunt")
	@DateTimeFormat (pattern = "yyyy-MM-dd")
	private LocalDate dernierEmprunt ;
	
	@Column(name = "premier_emprunt")
	@DateTimeFormat (pattern = "yyyy-MM-dd")
	private LocalDate premierEmprunt ;
	
	@Column(name = "date_fin_abonnement")
	@DateTimeFormat (pattern = "yyyy-MM-dd")
	private LocalDate finAbonnement ;
	
	@Column (name = "code_activation")
	private String codeActivation ;
	
	@Lob
	@Column (name = "photo")
	private byte[] photo ;
}
