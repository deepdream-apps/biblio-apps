package fr.cmci.biblio.operations.data;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

import fr.cmci.biblio.catalog.data.Livre;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Data
@EqualsAndHashCode (callSuper = false)
public class DetailPanier extends EntiteGenerique {
	@Id
	@GeneratedValue
	@Column (name = "id")
	private Long id ;
	
	@ManyToOne
	@JoinColumn(name = "id_livre")
	private Livre livre ;
	
	@Column(name = "date_ajout")
	@DateTimeFormat (pattern = "dd/MM/yyyy HH:mm")
	private LocalDateTime dateAjout ;

}
