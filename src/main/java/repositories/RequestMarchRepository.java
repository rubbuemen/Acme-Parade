
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.RequestMarch;

@Repository
public interface RequestMarchRepository extends JpaRepository<RequestMarch, Integer> {

	@Query("select rm from Procession p join p.requestsMarch rm where p.id = ?1 order by rm.status")
	Collection<RequestMarch> findRequestsMarchByProcessionOrderByStatus(int processionId);

	@Query("select rm from Member m join m.requestsMarch rm where m.id = ?1 order by rm.status")
	Collection<RequestMarch> findRequestsMarchByMemberOrderByStatus(int memberId);

	@Query("select distinct rm from Member m join m.enrolments e join e.brotherhood b join b.processions p join p.requestsMarch rm where e.momentDropOut is null and e.momentRegistered is not null and rm.member.id = ?1 and b.id = ?2")
	Collection<RequestMarch> findRequestsMarchMemberId(int memberId, int brotherhoodId);

	@Query("select rm from Procession p join p.requestsMarch rm where p.id = ?1 and rm.member.id = ?2 order by rm.status")
	Collection<RequestMarch> findRequestsMarchByProcessionMemberOrderByStatus(int processionId, int memberd);

	@Query("select rm from Procession p join p.requestsMarch rm where (rm.status = 'PENDING' or rm.status = 'APPROVED') and p.id = ?1 and rm.member.id = ?2")
	Collection<RequestMarch> findRequestMarchPendingOrApprovedByProcessionMember(int processionId, int memberd);

}
