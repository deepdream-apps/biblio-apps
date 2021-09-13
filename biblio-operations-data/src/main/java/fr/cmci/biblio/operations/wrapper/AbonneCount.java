package fr.cmci.biblio.operations.wrapper;
import fr.cmci.biblio.operations.data.Abonne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class AbonneCount {
	private Abonne abonne ;
	private Long nbDemandes ;
	
	public AbonneCount(Abonne abonne, Long nbDemandes) {
		super();
		this.abonne = abonne;
		this.nbDemandes = nbDemandes;
	}
	
}
