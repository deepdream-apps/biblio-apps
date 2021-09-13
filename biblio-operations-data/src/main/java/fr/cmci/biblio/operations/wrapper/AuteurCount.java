package fr.cmci.biblio.operations.wrapper;
import fr.cmci.biblio.catalog.data.Auteur;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class AuteurCount {
	private Auteur auteur ;
	private Long nbDemandes ;
	
	public AuteurCount(Auteur auteur, Long nbDemandes) {
		super();
		this.auteur = auteur;
		this.nbDemandes = nbDemandes;
	}
	
}
