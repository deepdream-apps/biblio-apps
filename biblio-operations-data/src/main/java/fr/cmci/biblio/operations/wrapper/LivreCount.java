package fr.cmci.biblio.operations.wrapper;

import fr.cmci.biblio.catalog.data.Livre;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Data
@EqualsAndHashCode
@NoArgsConstructor
public class LivreCount {
	private Livre livre ;
	private Long nbDemandes ;
	
	public LivreCount(Livre livre, Long nbDemandes) {
		super();
		this.livre = livre;
		this.nbDemandes = nbDemandes;
	}
	
}
