package fr.cmci.biblio.operations.wrapper;
import fr.cmci.biblio.catalog.data.Categorie;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Data
@EqualsAndHashCode
@NoArgsConstructor
public class CategorieCount {
	private Categorie categorie ;
	private Long nbDemandes ;
	
	public CategorieCount(Categorie categorie, Long nbDemandes) {
		super();
		this.categorie = categorie;
		this.nbDemandes = nbDemandes;
	}
	
}
