
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Chapter;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Integer> {

	@Query("select c from Chapter c join c.area a where a in (select a from Brotherhood b join b.parades p join b.area a where p.id = ?1)")
	Chapter findChapterByParadeId(int paradeId);

	@Query("select c from Chapter c join c.area a where a.id = ?1)")
	Chapter findChapterByAreaId(int areaId);

	@Query("select c from Chapter c join c.area a where a in (select a from Brotherhood b join b.area a where b.id = ?1)")
	Chapter findChapterByBrotherhoodId(int brotherhoodId);

}
