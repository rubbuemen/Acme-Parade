
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Area;

@Repository
public interface AreaRepository extends JpaRepository<Area, Integer> {

	@Query("select distinct a from Brotherhood b join b.area a where a != null")
	Collection<Area> findAreasBrotherhoodUsed();

	@Query("select a from Area a where a not in (select a from Chapter c join c.area a)")
	Collection<Area> findFreeAreas();
}
