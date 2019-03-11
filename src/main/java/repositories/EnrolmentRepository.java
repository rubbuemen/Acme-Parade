
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Enrolment;

@Repository
public interface EnrolmentRepository extends JpaRepository<Enrolment, Integer> {

	@Query("select e from Enrolment e where e.momentRegistered is null and e.brotherhood.id = ?1")
	Collection<Enrolment> findEnrolmentsPendingByBrotherhoodId(int brotherhoodId);

	@Query("select e from Enrolment e where e.momentDropOut is null and e.momentRegistered is not null and e.brotherhood.id = ?1 and e.member.id = ?2")
	Enrolment findEnrolmentPendingOfBrotherhoodByMemberId(int brotherhoodId, int memberId);

	@Query("select e from Enrolment e where e.momentDropOut is null and e.momentRegistered is null and e.brotherhood.id = ?1 and e.member.id = ?2")
	Enrolment findEnrolmentActualOfBrotherhoodByMemberId(int brotherhoodId, int memberId);

	@Query("select e from Enrolment e where e.momentDropOut is not null and e.brotherhood.id = ?1 and e.member.id = ?2")
	Collection<Enrolment> findEnrolmentsOfBrotherhoodByMemberId(int brotherhoodId, int memberId);

}
