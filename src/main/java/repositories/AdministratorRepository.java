
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Administrator;
import domain.Brotherhood;
import domain.Member;
import domain.Parade;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {

	@Query("select avg(1.0*(select count(e) from Enrolment e where e.brotherhood.id = b.id and e.momentDropOut is null and e.momentRegistered is not null)), min(1*(select count(e) from Enrolment e where e.brotherhood.id = b.id and e.momentDropOut is null and e.momentRegistered is not null)), max(1*(select count(e) from Enrolment e where e.brotherhood.id = b.id and e.momentDropOut is null and e.momentRegistered is not null)), stddev(1.0*(select count(e) from Enrolment e where e.brotherhood.id = b.id and e.momentDropOut is null and e.momentRegistered is not null)) from Brotherhood b")
	String dashboardQueryC1();

	@Query("select b from Brotherhood b join b.enrolments e join e.member m where e.momentDropOut is null group by b order by sum(m) desc")
	Collection<Brotherhood> dashboardQueryC2();

	@Query("select b from Brotherhood b join b.enrolments e join e.member m where e.momentDropOut is null group by b order by sum(m) asc")
	Collection<Brotherhood> dashboardQueryC3();

	@Query("select p1.title, rm1.status, 1.0*count(rm1)/(select count(rm2) from Parade p2 join p2.requestsMarch rm2 where p2 = p1) from Parade p1 join p1.requestsMarch rm1 group by rm1.status, p1 order by p1")
	Collection<Object[]> dashboardQueryC4();

	@Query("select p from Parade p where datediff(p.momentOrganise, CURRENT_DATE) <= 30")
	Collection<Parade> dashboardQueryC5();

	@Query("select rm1.status, 1.0*count(rm1)/(select count(rm2) from RequestMarch rm2) from RequestMarch rm1 group by rm1.status")
	Collection<Object[]> dashboardQueryC6();

	@Query("select m1 from Member m1 where (select count(rm) from Member m2 join m2.requestsMarch rm where rm.status = 'APPROVED' and m1 = m2) >= 1*(select 0.1*count(rm) from RequestMarch rm where rm.status = 'APPROVED')")
	Collection<Member> dashboardQueryC7();

	@Query("select e.positionBrotherhood.nameEnglish, e.positionBrotherhood.nameSpanish, count(e.positionBrotherhood) from Enrolment e where e.momentDropOut is null group by e.positionBrotherhood")
	Collection<Object[]> dashboardQueryC8();

	@Query("select 1.0*(select sum(1*(select count(b) from Brotherhood b where b.area.id = a.id))/count(distinct b) from Brotherhood b), 1*(select count(b) from Brotherhood b where b.area is not null), min(1*(select count(b) from Brotherhood b where b.area.id = a.id)), max(1*(select count(b) from Brotherhood b where b.area.id = a.id)), avg(1.0*(select count(b) from Brotherhood b where b.area.id = a.id)), stddev(1.0*(select count(b) from Brotherhood b where b.area.id = a.id)) from Area a")
	String dashboardQueryB1();

	@Query("select min(f.parades.size), max(f.parades.size), avg(f.parades.size), stddev(f.parades.size) from Finder f")
	String dashboardQueryB2();

	@Query("select sum(case when f.parades.size = 0 then 1.0 else 0.0 end)/sum(case when f.parades.size > 0 then 1.0 else 0.0 end) from Finder f")
	String dashboardQueryB3();

	@Query("select sum(case when a.isSpammer = 1 then 1.0 else 0.0 end)/(select count(a1) from Actor a1)*100, sum(case when a.isSpammer = 0 or a.isSpammer is null then 1.0 else 0.0 end)/(select count(a1) from Actor a1)*100 from Actor a")
	String dashboardQueryAPlus1();

	@Query("select avg(b.polarityScore), avg(m.polarityScore), avg(a.polarityScore) from Brotherhood b, Member m, Administrator a")
	Double[] dashboardQueryAPlus2();

}
