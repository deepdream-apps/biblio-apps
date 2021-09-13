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
public class Session extends EntiteGenerique{
	@Id
	@Column(name="id")
	private Long id ;
	
	@Column(name="num")
	private Long num ;
	
	@ManyToOne
	@JoinColumn(name = "id_utilisateur")
	private Utilisateur utilisateur ;
	
	@Column (name = "date_debut")
	@DateTimeFormat (pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime dateDebut ;
	
	@Column (name = "adresse_ip")
	private String adresseIP ;
	
	@Column (name = "localisation")
	private String localisation ;
	
	@Column (name = "equipement")
	private String equipement ;
	
	@Column (name = "date_fin")
	@DateTimeFormat (pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime dateFin ;
}
