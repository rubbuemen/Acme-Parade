/*
 * SampleTest.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package services;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Enrolment;
import domain.PositionBrotherhood;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class EnrolmentServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private EnrolmentService			enrolmentService;

	@Autowired
	private PositionBrotherhoodService	positionBrotherhoodService;

	@PersistenceContext
	EntityManager						entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 10.3 (Acme-Madrugá)
	 *         Caso de uso: listar "Enrolments" pendientes de la "Brotherhood" logeada
	 *         Tests positivos: 1
	 *         *** 1. Listar "Enrolments" correctamente
	 *         Tests negativos: 1
	 *         *** 1. Intento de listar "Enrolments" con una autoridad no permitida
	 *         Analisis de cobertura de sentencias: 100% 23/23 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverListEnrolmentsBrotherhood() {
		final Object testingData[][] = {
			{
				"brotherhood1", null
			}, {
				"chapter1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listEnrolmentsBrotherhoodTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 10.3 (Acme-Madrugá)
	 *         Caso de uso: inscribir un "Member" en un "Enrolment"
	 *         Tests positivos: 1
	 *         *** 1. Inscribir un "Member" de un "Enrolment" correctamente
	 *         Tests negativos: 3
	 *         *** 1. Intento de inscripción de un "Member" en un "Enrolment" con una autoridad no permitida
	 *         *** 2. Intento de inscripción de un "Member" en un "Enrolment" que no es del "Brotherhood" logeado
	 *         *** 3. Intento de inscripción de un "Member" en un "Enrolment" sin seleccionar la posición
	 *         Analisis de cobertura de sentencias: 87,5% 112/128 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverEnrollMember() {
		final Object testingData[][] = {
			{
				"brotherhood2", "enrolment9", "positionBrotherhood1", null
			}, {
				"chapter1", "enrolment9", "positionBrotherhood1", IllegalArgumentException.class
			}, {
				"brotherhood1", "enrolment9", "positionBrotherhood1", IllegalArgumentException.class
			}, {
				"brotherhood2", "enrolment9", null, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.enrollMemberTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 11.2 (Acme-Madrugá)
	 *         Caso de uso: logeado como "Member", abandonar una "Brotherhood" dejando su estado en el "Enrolment"
	 *         Tests positivos: 1
	 *         *** 1. Abandonar una "Brotherhood" correctamente
	 *         Tests negativos: 1
	 *         *** 1. Intento de abandonar una "Brotherhood" con una autoridad no permitida
	 *         Analisis de cobertura de sentencias: 87,9% 153/174 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverDropOutMemberBrotherhood() {
		final Object testingData[][] = {
			{
				"member1", "brotherhood1", null
			}, {
				"chapter1", "brotherhood1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.dropOutMemberBrotherhoodTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Template methods ------------------------------------------------------

	protected void listEnrolmentsBrotherhoodTemplate(final String username, final Class<?> expected) {
		Class<?> caught = null;
		Collection<Enrolment> enrolments;

		super.startTransaction();

		try {
			super.authenticate(username);
			enrolments = this.enrolmentService.findEnrolmentsPendingByBrotherhoodLogged();
			Assert.notNull(enrolments);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void enrollMemberTemplate(final String username, final String enrolment, final String positionBrotherhood, final Class<?> expected) {
		Class<?> caught = null;
		Enrolment enrolmentEntity;
		PositionBrotherhood positionBrotherhoodEntity = null;

		super.startTransaction();

		try {
			super.authenticate(username);
			enrolmentEntity = this.enrolmentService.findEnrolmentPenddingBrotherhoodLogged(super.getEntityId(enrolment));
			if (positionBrotherhood != null)
				positionBrotherhoodEntity = this.positionBrotherhoodService.findOne(super.getEntityId(positionBrotherhood));
			enrolmentEntity.setPositionBrotherhood(positionBrotherhoodEntity);
			enrolmentEntity = this.enrolmentService.save(enrolmentEntity);
			this.enrolmentService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void dropOutMemberBrotherhoodTemplate(final String username, final String brotherhood, final Class<?> expected) {
		Class<?> caught = null;

		super.startTransaction();

		try {
			super.authenticate(username);
			this.enrolmentService.dropOutOfBrotherhood(super.getEntityId(brotherhood));
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}
}
