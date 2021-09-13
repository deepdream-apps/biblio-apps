package fr.cmci.biblio.security.data;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
@MappedSuperclass
@Data
@EqualsAndHashCode (callSuper = false)
public class EntiteGenerique implements Serializable{
	@Column (name = "createur")
	private String createur ;
	
	@Column (name = "modificateur")
	private String modificateur ;
	
	@Column(name = "date_creation")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime dateCreation ;
	
	@Column(name = "date_dern_maj")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime dateDernMaj ;
}
