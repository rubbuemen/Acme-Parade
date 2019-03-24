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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Sponsor;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SponsorServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private SponsorService	sponsorService;

	@PersistenceContext
	EntityManager			entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 15.1 (Acme-Parade)
	 *         Caso de uso: registrarse como "Sponsor" en el sistema
	 *         Tests positivos: 1
	 *         *** 1. Registrarse como "Sponsor" correctamente
	 *         Tests negativos: 8
	 *         *** 1. Intento de registro como "Sponsor" con el nombre vacío
	 *         *** 2. Intento de registro como "Sponsor" con el apellido vacío
	 *         *** 3. Intento de registro como "Sponsor" con el email vacío
	 *         *** 4. Intento de registro como "Sponsor" con el email sin cumplir el patrón adecuado
	 *         *** 5. Intento de registro como "Sponsor" con el usuario vacío
	 *         *** 6. Intento de registro como "Sponsor" con tamaño del usuario menor a 5 caracteres
	 *         *** 7. Intento de registro como "Sponsor" con tamaño del usuario mayor a 32 caracteres
	 *         *** 8. Intento de registro como "Sponsor" usuario ya usado
	 *         Analisis de cobertura de sentencias: 92,3% 12/13 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverRegisterAsSponsor() {
		final Object testingData[][] = {
			{
				"testName", "testSurname", "testEmail@testemail.com", "testUser", "testPass", null
			}, {
				"", "testSurname", "testEmail@testemail.com", "testUser", "testPass", ConstraintViolationException.class
			}, {
				"testName", "", "testEmail@testemail.com", "testUser", "testPass", ConstraintViolationException.class
			}, {
				"testName", "testSurname", "", "testUser", "testPass", ConstraintViolationException.class
			}, {
				"testName", "testSurname", "testEmail", "testUser", "testPass", ConstraintViolationException.class
			}, {
				"testName", "testSurname", "testEmail@testemail.com", "", "testPass", ConstraintViolationException.class
			}, {
				"testName", "testSurname", "testEmail@testemail.com", "test", "testPass", ConstraintViolationException.class
			}, {
				"testName", "testSurname", "testEmail@testemail.com", "testUsertestUsertestUsertestUsertestUsertestUsertestUsertestUser", "testPass", ConstraintViolationException.class
			}, {
				"testName", "testSurname", "testEmail@testemail.com", "sponsor1", "testPass", DataIntegrityViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.registerAsSponsorTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 9.2 (Acme-Madrugá)
	 *         Caso de uso: editar sus datos estando logeado
	 *         Tests positivos: 1
	 *         *** 1. Editar sus datos correctamente
	 *         Tests negativos: 9
	 *         *** 1. Intento de edición de datos de un actor que no es el logeado
	 *         *** 2. Intento de edición como "Sponsor" con el nombre vacío
	 *         *** 3. Intento de edición como "Sponsor" con el apellido vacío
	 *         *** 4. Intento de edición como "Sponsor" con el email vacío
	 *         *** 5. Intento de edición como "Sponsor" con el email sin cumplir el patrón adecuado
	 *         Analisis de cobertura de sentencias: 92,3% 12/13 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverEditData() {
		final Object testingData[][] = {
			{
				"sponsor1", "sponsor1", "testName", "testSurname", "testEmail@testemail.com", null
			}, {
				"sponsor1", "sponsor2", "testName", "testSurname", "testEmail@testemail.com", IllegalArgumentException.class
			}, {
				"sponsor1", "sponsor1", "", "testSurname", "testEmail@testemail.com", ConstraintViolationException.class
			}, {
				"sponsor1", "sponsor1", "testName", "", "testEmail@testemail.com", ConstraintViolationException.class
			}, {
				"sponsor1", "sponsor1", "testName", "testSurname", "", ConstraintViolationException.class
			}, {
				"sponsor1", "sponsor1", "testName", "testSurname", "testEmail", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editDataTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	// Template methods ------------------------------------------------------

	protected void registerAsSponsorTemplate(final String name, final String surname, final String email, final String username, final String password, final Class<?> expected) {
		Class<?> caught = null;
		Sponsor sponsor;

		super.startTransaction();

		try {
			sponsor = this.sponsorService.create();
			sponsor.setName(name);
			sponsor.setSurname(surname);
			sponsor.setEmail(email);
			sponsor.getUserAccount().setUsername(username);
			sponsor.getUserAccount().setPassword(password);
			this.sponsorService.save(sponsor);
			this.sponsorService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.rollbackTransaction();
	}

	protected void editDataTemplate(final String username, final String actorData, final String name, final String surname, final String email, final Class<?> expected) {
		Class<?> caught = null;
		Sponsor sponsor;

		super.startTransaction();

		try {
			super.authenticate(username);
			sponsor = this.sponsorService.findOne(super.getEntityId(actorData));
			sponsor.setName(name);
			sponsor.setSurname(surname);
			sponsor.setEmail(email);
			this.sponsorService.save(sponsor);
			this.sponsorService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		super.unauthenticate();
		this.checkExceptions(expected, caught);
		super.rollbackTransaction();
	}
}
