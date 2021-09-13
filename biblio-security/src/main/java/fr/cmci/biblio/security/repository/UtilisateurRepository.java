package fr.cmci.biblio.security.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.cmci.biblio.security.data.Utilisateur;
@Repository
public interface UtilisateurRepository extends CrudRepository<Utilisateur, Long>{
	public Utilisateur findByEmail(String email) ;
}
