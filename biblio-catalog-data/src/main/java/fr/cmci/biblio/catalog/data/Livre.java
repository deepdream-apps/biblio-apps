package fr.cmci.biblio.catalog.data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode (callSuper = false)
public class Livre extends EntiteGenerique{
	@Id
	@GeneratedValue
	@Column (name = "id")
	private Long id ;
	
	@NotNull
	@Column (name = "titre")
	private String titre ;
	
	@Column (name = "description")
	private String description ;
	
	@Column (name = "annee_publication")
	private Integer anneePublication ;
	
	@Column (name = "nb_pages")
	private Integer nbPages ;
	
	@Column (name = "quantite")
	private Integer quantite ;
	
	@Column (name = "langue")
	private String langue ;
	
	@ManyToOne
	@JoinColumn (name = "id_categorie1")
	private Categorie categorie1 ;
	
	@ManyToOne
	@JoinColumn (name = "id_categorie2")
	private Categorie categorie2 ;
	
	@ManyToOne
	@JoinColumn (name = "id_auteur")
	private Auteur auteur ;
	
	@Lob
	@Column (name = "couverture")
	private byte[] couverture ;

}
