
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Float;

@Repository
public interface FloatRepository extends JpaRepository<Float, Integer> {

	@Query("select f from Brotherhood b join b.floats f where b.id = ?1")
	Collection<Float> findFloatsByBrotherhoodId(int brotherhoodId);
}
