package fr.cmci.biblio.operations.data;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import fr.cmci.biblio.catalog.data.Livre;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Data
@EqualsAndHashCode (callSuper = false)
public class Panier extends EntiteGenerique {
	@Id
	@GeneratedValue
	@Column (name = "id")
	private Long id ;
	
	@ManyToOne
	@JoinColumn(name = "id_abonne")
	private Abonne abonne ;
	
	@Column (name = "nb_livres")
	private Integer nbLivres ;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Livre> listeLivres = new ArrayList<Livre>() ;

}
