
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {

	@Query("select m from Brotherhood b join b.enrolments e join e.member m where b.id = ?1 and e.momentDropOut is null and e.momentRegistered is not null")
	Collection<Member> findMembersByBrotherhoodId(int brotherhoodId);

	@Query("select m from Brotherhood b join b.enrolments e join e.member m where b.id = ?1 and e.momentDropOut is not null")
	Collection<Member> findMembersByBrotherhoodIdAll(int brotherhoodId);

	@Query("select m from RequestMarch rm join rm.member m where rm.id = ?1")
	Member findMemberByRequestMarchId(int requestMarchId);

}
