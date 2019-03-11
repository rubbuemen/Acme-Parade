
package repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Parade;

@Repository
public interface ParadeRepository extends JpaRepository<Parade, Integer> {

	@Query("select p from Brotherhood b join b.parades p where b.id = ?1 and p.isFinalMode = 1")
	Collection<Parade> findParadesFinalModeByBrotherhoodId(int brotherhoodId);

	@Query("select p from Parade p where ?1 member of p.floats")
	Collection<Parade> findParadesByFloatId(int floatId);

	@Query("select p from Parade p join p.requestsMarch rm where rm.id = ?1")
	Parade findParadeByRequestMarchId(int requestMarchId);

	@Query("select p from Parade p where p.isFinalMode = 1 and (p.ticker like %?1% or p.title like %?1% or p.description like %?1%)")
	Collection<Parade> findParadesFilterByKeyWord(String keyWord);

	@Query("select p from Parade p where p.isFinalMode = 1 and p.momentOrganise between ?1 and ?2")
	Collection<Parade> findParadesFilterDate(Date minDate, Date maxDate);

	@Query("select distinct p from Brotherhood b join b.parades p where p.isFinalMode = 1 and b.area.id = ?1")
	Collection<Parade> findParadesFilterByAreaId(int areaId);

	@Query("select p from Parade p where p.isFinalMode = 1")
	Collection<Parade> findParadesFinalMode();

}
