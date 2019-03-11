
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.PositionBrotherhood;

@Repository
public interface PositionBrotherhoodRepository extends JpaRepository<PositionBrotherhood, Integer> {

	@Query("select e.positionBrotherhood from Brotherhood b join b.enrolments e where e.momentDropOut is null and e.momentRegistered is not null and e.member.id = ?1 and b.id = ?2")
	PositionBrotherhood findPositionBrotherhoodByMemberIdBrotherhoodId(int memberId, int brotherhoodId);

	@Query("select distinct pb from Enrolment e join e.positionBrotherhood pb where pb != null")
	Collection<PositionBrotherhood> findPositionsBrotherhoodUsed();

}
