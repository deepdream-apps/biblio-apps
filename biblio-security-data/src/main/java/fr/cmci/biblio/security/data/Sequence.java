package fr.cmci.biblio.security.data;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Data
@EqualsAndHashCode (callSuper = false)
public class Sequence implements Serializable{
	@Id
	@Column (name = "id")
	private long id ;
	
	@Column (name = "classname")
	private String classname ;
	
	@Column (name = "courant")
	private Long courant ;

}
