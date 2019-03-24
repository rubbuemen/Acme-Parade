/*
 * SampleTest.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package usecases;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import services.AdministratorService;
import services.BrotherhoodService;
import services.ChapterService;
import services.MemberService;
import services.SponsorService;
import utilities.AbstractTest;
import domain.Brotherhood;
import domain.Chapter;
import domain.Member;
import domain.Parade;
import domain.Sponsor;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class DashboardTest extends AbstractTest {

	// SUT Services
	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private MemberService			memberService;

	@Autowired
	private ChapterService			chapterService;

	@Autowired
	private SponsorService			sponsorService;

	@PersistenceContext
	EntityManager					entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisitos funcionales: 12.3 (Acme-Madrugá), 22.2 (Acme-Madrugá), 4.1 (Acme-Parade), 8.1 (Acme-Parade), 18.2 (Acme-Parade)
	 *         Caso de uso: mostrar una "dashboard"
	 *         Tests positivos: 1
	 *         *** 1. Mostrar una "dashboard" correctamente
	 *         Tests negativos: 1
	 *         *** 1. Intento de muestra de una "dashboard" con una autoridad no permitida
	 *         Analisis de cobertura de sentencias: 98,75% 396/401 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void driverDashboard() {
		final Collection<Parade> emptyParades = new HashSet<>();
		final Object testingData[][] = {
			{
				"admin", "0.875,0,4,1.53603", "brotherhood2,brotherhood1", "brotherhood1,brotherhood2", "0.55556,0.33333,0.11111,0.75,0.25,1.0", emptyParades, "0.4375,0.3125,0.25", "member1,member2,member3", "3,2,1,1", "0.875,7,0,2,1.0,0.53452",
				"0,3,1.2,1.1662", "0.66667", "0.0,100.0", "null,null,null", "4.1429,1,10,2.69542", "brotherhood1", "brotherhood1,brotherhood5", "0.42857", "1.6,0,7,2.72764", "chapter1", "0.14286", "0.71429,0.14286,0.14286", "1.0", "0.8,0,3,1.16619",
				"sponsor1,sponsor2", null
			},
			{
				"brotherhood1", "0.875,0,4,1.53603", "brotherhood2,brotherhood1", "brotherhood1,brotherhood2", "0.55556,0.33333,0.11111,0.75,0.25,1.0", emptyParades, "0.4375,0.3125,0.25", "member1,member2,member3", "3,2,1,1", "0.875,7,0,2,1.0,0.53452",
				"0,3,1.2,1.1662", "0.66667", "0.0,100.0", "null,null,null", "4.1429,1,10,2.69542", "brotherhood1", "brotherhood1,brotherhood5", "0.42857", "1.6,0,7,2.72764", "chapter1", "0.14286", "0.71429,0.14286,0.14286", "1.0", "0.8,0,3,1.16619",
				"sponsor1,sponsor2", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.dashboardTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Collection<Parade>) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (String) testingData[i][8], (String) testingData[i][9], (String) testingData[i][10], (String) testingData[i][11], (String) testingData[i][12], (String) testingData[i][13], (String) testingData[i][14],
				(String) testingData[i][15], (String) testingData[i][16], (String) testingData[i][17], (String) testingData[i][18], (String) testingData[i][19], (String) testingData[i][20], (String) testingData[i][21], (String) testingData[i][22],
				(String) testingData[i][23], (String) testingData[i][24], (Class<?>) testingData[i][25]);
	}

	// Template methods ------------------------------------------------------

	protected void dashboardTemplate(final String username, final String dashboardQueryC1, final String dashboardQueryC2, final String dashboardQueryC3, final String dashboardQueryC4, final Collection<Parade> dashboardQueryC5,
		final String dashboardQueryC6, final String dashboardQueryC7, final String dashboardQueryC8, final String dashboardQueryB1, final String dashboardQueryB2, final String dashboardQueryB3, final String dashboardQueryAPlus1,
		final String dashboardQueryAPlus2, final String dashboardQueryAcmeParadeC1, final String dashboardQueryAcmeParadeC2, final String dashboardQueryAcmeParadeC3, final String dashboardQueryAcmeParadeB1, final String dashboardQueryAcmeParadeB2,
		final String dashboardQueryAcmeParadeB3, final String dashboardQueryAcmeParadeB4, final String dashboardQueryAcmeParadeB5, final String dashboardQueryAcmeParadeA1, final String dashboardQueryAcmeParadeA2, final String dashboardQueryAcmeParadeA3,
		final Class<?> expected) {
		Class<?> caught = null;
		final Collection<Brotherhood> brotherhoods = new HashSet<>();
		final Collection<Member> members = new HashSet<>();
		final Collection<Chapter> chapters = new HashSet<>();
		final Collection<Sponsor> sponsors = new HashSet<>();
		String results = "";

		super.startTransaction();

		try {
			super.authenticate(username);

			//Query C1
			Assert.isTrue(dashboardQueryC1.equals(this.administratorService.dashboardQueryC1()));

			//Query C2
			final String[] queryC2Dashboard = dashboardQueryC2.split(",");
			for (final String brotherhood : queryC2Dashboard) {
				final Brotherhood b = this.brotherhoodService.findOne(super.getEntityId(brotherhood));
				brotherhoods.add(b);
			}

			Assert.isTrue(brotherhoods.containsAll(this.administratorService.dashboardQueryC2()));
			brotherhoods.clear();

			//Query C3
			final String[] queryC3Dashboard = dashboardQueryC3.split(",");
			for (final String brotherhood : queryC3Dashboard) {
				final Brotherhood b = this.brotherhoodService.findOne(super.getEntityId(brotherhood));
				brotherhoods.add(b);
			}

			Assert.isTrue(brotherhoods.containsAll(this.administratorService.dashboardQueryC3()));
			brotherhoods.clear();

			//Query C4
			final Collection<Object[]> queryC4 = this.administratorService.dashboardQueryC4();
			for (final Object[] o : queryC4)
				results = results + o[2].toString() + ",";
			results = results.substring(0, results.length() - 1);
			Assert.isTrue(dashboardQueryC4.equals(results));
			results = "";

			//Query C5
			Assert.isTrue(dashboardQueryC5.containsAll(this.administratorService.dashboardQueryC5()));

			//Query C6
			final Collection<Object[]> queryC6 = this.administratorService.dashboardQueryC6();
			for (final Object[] o : queryC6)
				results = results + o[1].toString() + ",";
			results = results.substring(0, results.length() - 1);
			Assert.isTrue(dashboardQueryC6.equals(results));
			results = "";

			//Query C7
			final String[] queryC7Dashboard = dashboardQueryC7.split(",");
			for (final String member : queryC7Dashboard) {
				final Member m = this.memberService.findOne(super.getEntityId(member));
				members.add(m);
			}

			Assert.isTrue(members.containsAll(this.administratorService.dashboardQueryC7()));
			members.clear();

			//Query C8
			final Collection<Object[]> queryC8 = this.administratorService.dashboardQueryC8();
			for (final Object[] o : queryC8)
				results = results + o[2].toString() + ",";
			results = results.substring(0, results.length() - 1);
			Assert.isTrue(dashboardQueryC8.equals(results));
			results = "";

			//Query B1
			Assert.isTrue(dashboardQueryB1.equals(this.administratorService.dashboardQueryB1()));

			//Query B2
			Assert.isTrue(dashboardQueryB2.equals(this.administratorService.dashboardQueryB2()));

			//Query B3
			Assert.isTrue(dashboardQueryB3.equals(this.administratorService.dashboardQueryB3()));

			//Query A+1
			Assert.isTrue(dashboardQueryAPlus1.equals(this.administratorService.dashboardQueryAPlus1()));

			//Query A+2
			final Double[] queryAPlus2 = this.administratorService.dashboardQueryAPlus2();
			for (final Double o : queryAPlus2)
				if (o == null)
					results = results + "null" + ",";

			results = results.substring(0, results.length() - 1);
			Assert.isTrue(dashboardQueryAPlus2.equals(results));
			results = "";

			//Query C1 (Acme-Parade)
			Assert.isTrue(dashboardQueryAcmeParadeC1.equals(this.administratorService.dashboardQueryAcmeParadeC1()));

			//Query C2 (Acme-Parade)
			final Brotherhood bro = this.brotherhoodService.findOne(super.getEntityId(dashboardQueryAcmeParadeC2));
			Assert.isTrue(bro.equals(this.administratorService.dashboardQueryAcmeParadeC2()));

			//Query C3 (Acme-Parade)
			final String[] queryAcmeParadeC3Dashboard = dashboardQueryAcmeParadeC3.split(",");
			for (final String brotherhood : queryAcmeParadeC3Dashboard) {
				final Brotherhood b = this.brotherhoodService.findOne(super.getEntityId(brotherhood));
				brotherhoods.add(b);
			}

			Assert.isTrue(brotherhoods.containsAll(this.administratorService.dashboardQueryAcmeParadeC3()));
			brotherhoods.clear();

			//Query B1 (Acme-Parade)
			Assert.isTrue(dashboardQueryAcmeParadeB1.equals(this.administratorService.dashboardQueryAcmeParadeB1()));

			//Query B2 (Acme-Parade)
			Assert.isTrue(dashboardQueryAcmeParadeB2.equals(this.administratorService.dashboardQueryAcmeParadeB2()));

			//Query B3 (Acme-Parade)
			final String[] queryAcmeParadeB3Dashboard = dashboardQueryAcmeParadeB3.split(",");
			for (final String chapter : queryAcmeParadeB3Dashboard) {
				final Chapter c = this.chapterService.findOne(super.getEntityId(chapter));
				chapters.add(c);
			}

			Assert.isTrue(chapters.containsAll(this.administratorService.dashboardQueryAcmeParadeB3()));
			chapters.clear();

			//Query B4 (Acme-Parade)
			Assert.isTrue(dashboardQueryAcmeParadeB4.equals(this.administratorService.dashboardQueryAcmeParadeB4()));

			//Query B5 (Acme-Parade)
			final Collection<Object[]> queryAcmeParadeB5 = this.administratorService.dashboardQueryAcmeParadeB5();
			for (final Object[] o : queryAcmeParadeB5)
				results = results + o[1].toString() + ",";
			results = results.substring(0, results.length() - 1);
			Assert.isTrue(dashboardQueryAcmeParadeB5.equals(results));
			results = "";

			//Query A1 (Acme-Parade)
			Assert.isTrue(dashboardQueryAcmeParadeA1.equals(this.administratorService.dashboardQueryAcmeParadeA1()));

			//Query A2 (Acme-Parade)
			Assert.isTrue(dashboardQueryAcmeParadeA2.equals(this.administratorService.dashboardQueryAcmeParadeA2()));

			//Query A3 (Acme-Parade)
			final String[] queryAcmeParadeA3Dashboard = dashboardQueryAcmeParadeA3.split(",");
			for (final String sponsor : queryAcmeParadeA3Dashboard) {
				final Sponsor s = this.sponsorService.findOne(super.getEntityId(sponsor));
				sponsors.add(s);
			}

			Assert.isTrue(sponsors.containsAll(this.administratorService.dashboardQueryAcmeParadeA3()));
			sponsors.clear();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}
}
