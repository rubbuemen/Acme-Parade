
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Brotherhood;

@Repository
public interface BrotherhoodRepository extends JpaRepository<Brotherhood, Integer> {

	@Query("select b from Brotherhood b where ?1 member of b.floats")
	Brotherhood findBrotherhoodByFloatId(int floatId);

	@Query("select b from Brotherhood b where ?1 member of b.parades")
	Brotherhood findBrotherhoodByParadeId(int paradeId);

	@Query("select distinct b from Brotherhood b join b.enrolments e where e.momentDropOut is null and e.momentRegistered is not null and e.member.id = ?1")
	Collection<Brotherhood> findBrotherhoodsByMemberId(int memberId);

	@Query("select b from Brotherhood b where ?1 member of b.enrolments")
	Brotherhood findBrotherhoodByEnrolmentId(int enrolmentId);

	@Query("select b from Brotherhood b join b.parades p where ?1 member of p.requestsMarch")
	Brotherhood findBrotherhoodByRequestMarchId(int requestMarchId);

	@Query("select distinct b from Brotherhood b join b.enrolments e where e.momentDropOut is not null and e.member.id = ?1")
	Collection<Brotherhood> findBrotherhoodsNotBelongsByMemberLogged(int memberId);

	@Query("select distinct b from Brotherhood b join b.enrolments e where e.momentDropOut is null and e.member.id = ?1")
	Collection<Brotherhood> findBrotherhoodsAcceptedOrPendingByMemberId(int memberId);

	@Query("select h.brotherhood from History h join h.inceptionRecord i where i.id = ?1")
	Brotherhood findBrotherhoodByInceptionRecordId(int inceptionRecordId);

	@Query("select h.brotherhood from History h join h.periodRecords p where p.id = ?1")
	Brotherhood findBrotherhoodByPeriodRecordId(int periodRecordId);

	@Query("select h.brotherhood from History h join h.legalRecords l where l.id = ?1")
	Brotherhood findBrotherhoodByLegalRecordId(int legalRecordId);

	@Query("select h.brotherhood from History h join h.linkRecords l where l.id = ?1")
	Brotherhood findBrotherhoodByLinkRecordId(int linkRecordId);

	@Query("select h.brotherhood from History h join h.miscellaneousRecords m where m.id = ?1")
	Brotherhood findBrotherhoodByMiscellaneousRecordId(int miscellaneousRecordId);

	@Query("select b from Brotherhood b join b.area a where a in (select a from Chapter c join c.area a where c.id = ?1)")
	Collection<Brotherhood> findBrotherhoodsByChapterId(int chapterId);

}
