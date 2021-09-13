package fr.cmci.biblio.operations.data;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode (callSuper = false)
public class Souscription extends EntiteGenerique{
	@Id
	@GeneratedValue
	@Column (name = "id")
	private Long id ;
	
	@ManyToOne
	@JoinColumn(name = "id_abonne")
	private Abonne abonne ;
	
	@ManyToOne
	@JoinColumn(name = "id_paiement")
	private Paiement paiement ;
	
	@Column (name = "nb_annees")
	private Integer nbAnnees ;
	
	@Column(name = "date_debut")
	@DateTimeFormat (pattern = "yyyy-MM-dd")
	private LocalDate dateDebut ;
	

	@Column(name = "date_fin")
	@DateTimeFormat (pattern = "yyyy-MM-dd")
	private LocalDate dateFin ;
	

}
