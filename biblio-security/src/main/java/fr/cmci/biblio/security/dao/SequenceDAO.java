package fr.cmci.biblio.security.dao;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import fr.cmci.biblio.security.data.Sequence;
@Transactional
@Component
public class SequenceDAO {
	
	@PersistenceContext
	private EntityManager entityManager ;
	
	public long nextId(String className) throws Exception {
		String qs = "select s from Sequence s where s.classname=:classname" ;
		Query query = entityManager.createQuery(qs) ;
		query.setParameter("classname", className) ;
		List<Sequence> seqs = query.getResultList() ;
		if(seqs.size() == 0 ) {
			Sequence seq = new Sequence() ;
			seq.setId(System.currentTimeMillis());
			seq.setClassname(className);
			seq.setCourant(10000000L);
			entityManager.persist(seq) ;
			return 10000000L ;
		} else {
			Sequence seq = seqs.get(0) ;
			seq.setCourant(seq.getCourant() + 1);
			entityManager.persist(seq);
			return seq.getCourant() ;
		}
	}
}
