package fr.cmci.biblio.operations.data;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode (callSuper = false)
public class Offre extends EntiteGenerique{
	@Id
	@Column (name = "id")
	private Long id ;
	
	@NotNull
	@Column (name = "libelle")
	private String libelle ;
	
	@Column (name = "nb_livres")
	private Integer nbLivres ;
	
	@Column (name = "montant")
	private BigDecimal montant ;
}
