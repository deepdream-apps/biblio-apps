package fr.cmci.biblio.security.data;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode (callSuper = false)
public class Evenement extends EntiteGenerique{
	@Id
	@Column (name = "id")
	private Long id ;
	
	@ManyToOne
	@JoinColumn (name = "id_utilisateur")
	private Utilisateur utilisateur ;
	
	@Column (name = "date_journal")
	@DateTimeFormat (pattern = "yyyy-MM-dd")
	private LocalDateTime date ;
	
	@Column (name = "adresse_ip")
	private String adresseIp ;
	
	@Column (name = "localisation")
	private String localisation ;
	
	@Column (name = "resultat")
	private Integer resultat ;
}
