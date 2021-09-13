package fr.cmci.biblio.catalog.data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode (callSuper = false)
public class Auteur extends EntiteGenerique{
	@Id
	@GeneratedValue
	@Column (name = "id")
	private Long id ;
	
	@NotNull
	@Column (name = "nom")
	private String nom ;
	
	@Column (name = "description")
	private String description ;
	
	@Column (name = "annee_naissance")
	private Integer anneeNaissance ;
	
	@Column (name = "annee_deces")
	private Integer anneeDeces ;
	
	@Column (name = "biographie_1")
	private String biographie1 ;
	
	@Column (name = "biographie_2")
	private String biographie2 ;
	
	@Lob
	@Column (name = "photo")
	private byte[] photo ;
	
	@Column (name = "pays")
	private String pays ;
	
	@Column (name = "ordre")
	private Long ordre ;
}
