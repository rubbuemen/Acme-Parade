
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Administrator;
import domain.Brotherhood;
import domain.Chapter;
import domain.Member;
import domain.Parade;
import domain.Sponsor;

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

	@Query("select avg(h.periodRecords.size + h.legalRecords.size + h.linkRecords.size + h.miscellaneousRecords.size + 1), min(h.periodRecords.size + h.legalRecords.size + h.linkRecords.size + h.miscellaneousRecords.size + 1), max(h.periodRecords.size + h.legalRecords.size + h.linkRecords.size + h.miscellaneousRecords.size + 1), stddev(h.periodRecords.size + h.legalRecords.size + h.linkRecords.size + h.miscellaneousRecords.size + 1.0) from History h")
	String dashboardQueryAcmeParadeC1();

	@Query("select b from History h1 join h1.brotherhood b where (select h2.periodRecords.size + h2.legalRecords.size + h2.linkRecords.size + h2.miscellaneousRecords.size + 1 from History h2 where h1 = h2) = 1*(select max(h.periodRecords.size + h.legalRecords.size + h.linkRecords.size + h.miscellaneousRecords.size + 1) from History h)")
	Collection<Brotherhood> dashboardQueryAcmeParadeC2();

	@Query("select b from History h1 join h1.brotherhood b where (select h2.periodRecords.size + h2.legalRecords.size + h2.linkRecords.size + h2.miscellaneousRecords.size + (case when h2.inceptionRecord is not null then 1.0 else 0.0 end) from History h2 where h1 = h2) > 1.0*(select avg(h.periodRecords.size + h.legalRecords.size + h.linkRecords.size + h.miscellaneousRecords.size + 1) from History h)")
	Collection<Brotherhood> dashboardQueryAcmeParadeC3();

	@Query("select 1.0*count(a1)/(select count(a) from Area a) from Area a1 where a1 not in (select a from Chapter c join c.area a)")
	String dashboardQueryAcmeParadeB1();

	@Query("select avg(1.0*(select count(p1) from Brotherhood b join b.parades p1 join b.area a where a in (select a from Chapter c1 join c1.area a where c1.id = c2.id ))), min(1*(select count(p1) from Brotherhood b join b.parades p1 join b.area a where a in (select a from Chapter c1 join c1.area a where c1.id = c2.id ))), max(1*(select count(p1) from Brotherhood b join b.parades p1 join b.area a where a in (select a from Chapter c1 join c1.area a where c1.id = c2.id ))), stddev(1.0*(select count(p1) from Brotherhood b join b.parades p1 join b.area a where a in (select a from Chapter c1 join c1.area a where c1.id = c2.id ))) from Chapter c2")
	String dashboardQueryAcmeParadeB2();

	@Query("select c2 from Chapter c2 where (select count(p1) from Brotherhood b join b.parades p1 join b.area a where a in (select a from Chapter c1 join c1.area a where c1.id = c2.id )) >= 1.1*(select avg(1.0*(select count(p1) from Brotherhood b join b.parades p1 join b.area a where a in (select a from Chapter c1 join c1.area a where c1.id = c2.id ))) from Chapter c2)")
	Collection<Chapter> dashboardQueryAcmeParadeB3();

	@Query("select sum(case when p.isFinalMode = 0 then 1.0 else 0.0 end)/sum(case when p.isFinalMode = 1 then 1.0 else 0.0 end) from Parade p")
	String dashboardQueryAcmeParadeB4();

	@Query("select p1.status, 1.0*count(p1)/(select count(p2) from Parade p2 where p2.isFinalMode = 1) from Parade p1 where p1.isFinalMode = 1 group by p1.status")
	Collection<Object[]> dashboardQueryAcmeParadeB5();

	@Query("select 1.0*count(ss1)/(select count(ss) from Sponsorship ss) from Sponsorship ss1 where ss1.isActivated = 1")
	String dashboardQueryAcmeParadeA1();

	@Query("select avg(1.0*(select count(ss) from Sponsor s1 join s1.sponsorships ss where ss.sponsor.id = s.id and ss.isActivated = 1)), min(1*(select count(ss) from Sponsor s1 join s1.sponsorships ss where ss.sponsor.id = s.id and ss.isActivated = 1)), max(1*(select count(ss) from Sponsor s1 join s1.sponsorships ss where ss.sponsor.id = s.id and ss.isActivated = 1)), stddev(1.0*(select count(ss) from Sponsor s1 join s1.sponsorships ss where ss.sponsor.id = s.id and ss.isActivated = 1)) from Sponsor s")
	String dashboardQueryAcmeParadeA2();

	@Query("select s from Sponsor s join s.sponsorships ss where ss.isActivated = 1 group by s order by sum(ss) desc")
	Collection<Sponsor> dashboardQueryAcmeParadeA3();

}
