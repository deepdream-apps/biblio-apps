package fr.cmci.biblio.operations.data;
import java.time.LocalDateTime;
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

import org.springframework.format.annotation.DateTimeFormat;

import fr.cmci.biblio.catalog.data.Livre;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode (callSuper = false)
public class DetailPret extends EntiteGenerique{
	@Id
	@GeneratedValue
	@Column (name = "id")
	private Long id ;
	
	@ManyToOne
	@JoinColumn(name = "id_pret")
	private Pret pret ;
	
	@ManyToOne
	@JoinColumn(name = "id_livre")
	private Livre livre ;

	@Column(name = "date_retour_prevu")
	@DateTimeFormat (pattern = "dd/MM/yyyy HH:mm")
	private LocalDateTime dateRetourPrevu ;
	
	@Column(name = "date_retour")
	@DateTimeFormat (pattern = "dd/MM/yyyy HH:mm")
	private LocalDateTime dateRetour ;
	
	@Column (name = "quantite")
	private Integer quantite ;

	@Column (name = "statut")
	private String statut ;
	
}
